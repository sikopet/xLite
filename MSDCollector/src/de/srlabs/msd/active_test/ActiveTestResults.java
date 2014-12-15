package de.srlabs.msd.active_test;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Vector;

import android.annotation.SuppressLint;
import android.telephony.TelephonyManager;
import de.srlabs.msd.util.MsdLog;
import de.srlabs.msd.util.Utils;

public class ActiveTestResults implements Serializable {
	private static final long serialVersionUID = 1L;
	enum State{API_RUNNING, WAITING, TEST_RUNNING, SUCCESS, FAILED_TIMEOUT, FAILED_API_ERROR, FAILED_API_TIMEOUT, FAILED};
	private HashMap<String, NetworkOperatorTestResults> networkOperators = new HashMap<String, ActiveTestResults.NetworkOperatorTestResults>();
	private String currentMccMnc = null;
	private SingleTestState currentTest;
	private int numIterations = 5;
	private String fatalError = null;
	private boolean testRoundComplete = false;
	private String errorLog = "";
	private boolean onlineMode = true;
	private boolean blacklisted = false;

	class NetworkOperatorTestResults implements Serializable{
		private static final long serialVersionUID = 1L;
		private String mccMnc = null;
		private String operatorName = null;
		NetworkOperatorRatTestResults results2g = new NetworkOperatorRatTestResults(2);
		NetworkOperatorRatTestResults results3g = new NetworkOperatorRatTestResults(3);
		public int currentGeneration;
		public NetworkOperatorRatTestResults getGeneration(int generation){
			if(generation == 2)
				return results2g;
			else if(generation == 3)
				return results3g;
			else
				throw new IllegalStateException("Generation " + generation + " not supported");
		}
		public NetworkOperatorRatTestResults getCurrentGeneration() {
			return getGeneration(currentGeneration);
		}
		public void formatTable(StringBuffer result) {
			result.append("Network " + operatorName + ": " + mccMnc + "\n");
			String format = "%-10s%-10s%-10s\n";
			result.append(String.format(format,"Test", currentGeneration == 2 ? "*GSM*" : "GSM",currentGeneration == 3 ? "*3G*" : "3G"));
			result.append(String.format(format,"SMS out",getGeneration(2).formatCounts(TestType.SMS_MO),getGeneration(3).formatCounts(TestType.SMS_MO)));
			result.append(String.format(format,"Call out",getGeneration(2).formatCounts(TestType.CALL_MO),getGeneration(3).formatCounts(TestType.CALL_MO)));
			result.append(String.format(format,"SMS in",getGeneration(2).formatCounts(TestType.SMS_MT),getGeneration(3).formatCounts(TestType.SMS_MT)));
			result.append(String.format(format,"Call in",getGeneration(2).formatCounts(TestType.CALL_MT),getGeneration(3).formatCounts(TestType.CALL_MT)));
			result.append("\n");
		}
	}
	class NetworkOperatorRatTestResults  implements Serializable{
		private static final long serialVersionUID = 1L;
		int generation;
		HashMap<TestType,Vector<SingleTestState>> tests;
		HashMap<TestType,Integer> nextTestNumer = new HashMap<TestType, Integer>();
		public NetworkOperatorRatTestResults(int generation) {
			this.generation = generation;
			clearResults();
		}
		public String formatCounts(TestType type) {
			return "+" + getNumSuccess(type) + " -" + getNumFailures(type);			
		}
		public int getNumSuccess(TestType type){
			int result = 0;
			for(SingleTestState state:tests.get(type)){
				if(state.isSuccess())
					result++;
			}
			return result;
		}		
		public int getNumFailures(TestType type){
			int result = 0;
			for(SingleTestState state:tests.get(type)){
				if(state.isFailure())
					result++;
			}
			return result;
		}
		public int getNumRuns(TestType type){
			return tests.get(type).size();
		}
		public void add(SingleTestState newTest) {
			// Make sure that the test run number is unique even when pressing startover to repeat failed tests
			if(nextTestNumer.containsKey(newTest.type)){
				newTest.num = nextTestNumer.get(newTest.type);
			} else{
				newTest.num = 0;
			}
			nextTestNumer.put(newTest.type, newTest.num+1);
			tests.get(newTest.type).add(newTest);
		}
		public void clearFails(){
			for(TestType testType:TestType.values()){
				for(int i=0;i<tests.get(testType).size();i++){
					SingleTestState test = tests.get(testType).get(i);
					if(!test.isSuccess()){
						tests.get(testType).remove(i);
						i--;
					} else{
						test.num = i;
					}
				}
			}
		}
		public void clearResults() {
			tests = new HashMap<TestType, Vector<SingleTestState>>();
			tests.put(TestType.SMS_MO, new Vector<ActiveTestResults.SingleTestState>());
			tests.put(TestType.CALL_MO, new Vector<ActiveTestResults.SingleTestState>());
			tests.put(TestType.SMS_MT, new Vector<ActiveTestResults.SingleTestState>());
			tests.put(TestType.CALL_MT, new Vector<ActiveTestResults.SingleTestState>());
		}
	}
	class SingleTestState implements Serializable{
		private static final long serialVersionUID = 1L;
		private long startTime = 0;
		private long timeoutEndTime = 0;
		private long endTime = 0;
		TestType type;
		State state;
		String errorStr = null;
		private String requestId;
		private int num;
		public long timeoutStartTime;
		public SingleTestState(TestType type) {
			this.type = type;
		}
		@Override
		public String toString() {
			StringBuffer result = new StringBuffer("Test " + type.name() + "  State " + state.name());
			result.append("  START: " + Utils.formatTimestamp(startTime));
			if(endTime > 0)
				result.append("  END: " + Utils.formatTimestamp(startTime));
			if(errorStr != null)
				result.append("  Error: " + errorStr);
			return result.toString();
		}
		public void stateApiRunning(){
			state = State.API_RUNNING;
		}
		public void stateWaiting() {
			state = State.WAITING;
		}
		public void stateTestRunning(){
			state = State.TEST_RUNNING;
		}
		public String getApiId(){
			return requestId;
		}
		public boolean isFailure() {
			if(state == State.FAILED_TIMEOUT)
				return true;
			if(state == State.FAILED)
				return true;
			if(state == State.FAILED_API_ERROR)
				return true;
			if(state == State.FAILED_API_TIMEOUT)
				return true;
			return false;
		}
		public boolean isRunning(){
			return state == State.API_RUNNING || state == State.TEST_RUNNING || state == State.WAITING;
		}
		public boolean isSuccess() {
			return state == State.SUCCESS;
		}
		public void success() {
			state = State.SUCCESS;
		}
		public void failApiTimeout(){
			appendErrorLog("API timeout in " + type.name());
			state = State.FAILED_API_TIMEOUT;
		}
		public void failTimeout() {
			if(state == State.WAITING){
				if(type == TestType.CALL_MO){
					appendErrorLog("Timeout while dialing");
				} else if(type == TestType.SMS_MT){
					String msg = "Timeout while waiting for incoming SMS";
					if(requestId != null)
						msg += "  Request ID: " + requestId;
					appendErrorLog(msg);
				} else if(type == TestType.CALL_MT){
					String msg = "Timeout while waiting for incoming CALL";
					if(requestId != null)
						msg += "  Request ID: " + requestId;
					appendErrorLog(msg);
				} else{
					appendErrorLog("failTimeout INVALID:" + type.name() + " : " + state.name());
				}
			} else if(state == State.TEST_RUNNING){
				if(type == TestType.SMS_MO){
					appendErrorLog("Timeout while sending SMS to invalid destination");
				} else if(type == TestType.CALL_MO){
					appendErrorLog("Timeout while outgoing call is ringing");
				} else if(type == TestType.CALL_MT){
					appendErrorLog("Timeout while incoming call is ringing");
				} else{
					appendErrorLog("failTimeout INVALID:" + type.name() + " : " + state.name());
				}
			} else{
				appendErrorLog("failTimeout INVALID:" + type.name() + " : " + state.name());
			}
			state = State.FAILED_TIMEOUT;
		}
		public void failApiError(String requestId, String errorStr){
			state = State.FAILED_API_ERROR;
			this.errorStr = errorStr;
			this.requestId = requestId;
			String logMsg = "API failed in " + type.name() + "  id=" + requestId;
			if(errorStr != null && errorStr.trim().length()>0)
				logMsg += "  MSG: " + errorStr;
			appendErrorLog(logMsg);
		}
		public void fail(String errorStr){
			state = State.FAILED;
			this.errorStr = errorStr;
			appendErrorLog(errorStr);
		}
		public void setRequestId(String requestId) {
			this.requestId = requestId;
		}
		/**
		 * Retrieves the text to be displayed for the currently running test, only valid when isRunning() == true
		 * @return
		 */
		public String getStateDisplayText(){
			if(state == State.API_RUNNING){
				return "Test running";
			} else if(state == State.WAITING){
				if(type == TestType.SMS_MO){
					return "INVALID:" + type.name() + " : " + state.name();
				} else if(type == TestType.CALL_MO){
					return "Dialing";
				} else if(type == TestType.SMS_MT){
					return "Waiting for incoming SMS";
				} else if(type == TestType.CALL_MT){
					return "Waiting for incoming CALL";
				} else{
					return "INVALID:" + type.name() + " : " + state.name();					
				}
			} else if(state == State.TEST_RUNNING){
				if(type == TestType.SMS_MO){
					return "Sending SMS to invalid destination";
				} else if(type == TestType.CALL_MO){
					return "Outgoing call ringing";
				} else if(type == TestType.SMS_MT){
					return "INVALID:" + type.name() + " : " + state.name();	
				} else if(type == TestType.CALL_MT){
					return "Incoming call ringing";
				} else{
					return "INVALID:" + type.name() + " : " + state.name();					
				}
			}
			return "INVALID:" + type.name() + " : " + state.name();
		}

		/**
		 * For tests requiring an API request the initial timeout is the sum of
		 * the API timeout + the timeout for the actual test. After the API
		 * succeeded, the timeout should be updated.
		 * 
		 * @param timeoutMillisFromNow
		 */
		public void updateTimeout(long timeoutMillisFromNow) {
			timeoutEndTime = System.currentTimeMillis() + timeoutMillisFromNow;
		}
		public int getNum() {
			return num;
		}
		public int getProgressPercent() {
			long currentTime = System.currentTimeMillis();
			double progress = (double)(currentTime - timeoutStartTime) / (double)(timeoutEndTime - timeoutStartTime);
			MsdLog.i("ActiveTestResults","TIMEOUT_CALC: current=" + currentTime + "  timeoutStartTime=" + timeoutStartTime + "  timeoutEndTime=" + timeoutEndTime + "  => progress=" + progress);
			if(progress > 1.0)
				progress = 1.0;
			return (int)(100.0*progress);
		}
	}
	public void setNetworkOperatorAndRat(TelephonyManager tm, int fallbackNetworkGeneration) throws IllegalArgumentException{
		this.currentMccMnc = tm.getNetworkOperator();
		// MsdLog.i("active-test-results",currentMccMnc);
		NetworkOperatorTestResults networkOperator;
		if(networkOperators.containsKey(currentMccMnc)){
			networkOperator = networkOperators.get(currentMccMnc);
		} else{
			networkOperator = new NetworkOperatorTestResults();
			networkOperator.mccMnc = currentMccMnc;
			networkOperator.operatorName = tm.getNetworkOperatorName();
			networkOperators.put(networkOperator.mccMnc, networkOperator);
		}
		int networkGeneration = Utils.networkTypeToNetworkGeneration(tm.getNetworkType());
		if(networkGeneration == 0)
			networkGeneration = fallbackNetworkGeneration;
		if(networkGeneration == 4){
			throw new IllegalArgumentException("LTE is not yet supported");
		}
		networkOperator.currentGeneration = networkGeneration;
	}
	public void setDummyNetworkOperatorAndRat(){
		this.currentMccMnc = "31337";
		if(!networkOperators.containsKey(currentMccMnc)){
			NetworkOperatorTestResults networkOperator = new NetworkOperatorTestResults();
			networkOperator.mccMnc = currentMccMnc;
			networkOperator.operatorName = "Dummy Operator";
			networkOperator.currentGeneration = 3;
			networkOperators.put(networkOperator.mccMnc, networkOperator);
		}
	}
	public SingleTestState getCurrentTest(){
		return currentTest;
	}
	public NetworkOperatorTestResults getCurrentNetworkOperator(){
		return networkOperators.get(currentMccMnc);
	}
	public NetworkOperatorRatTestResults getCurrentNetworkOperatorRatTestResults(){
		NetworkOperatorTestResults currentOperator = getCurrentNetworkOperator();
		if(currentOperator == null)
			return null;
		return currentOperator.getCurrentGeneration();
	}
	public SingleTestState startTest(TestType type, long timeoutMillis){
		testRoundComplete = false;
		currentTest = new SingleTestState(type);
		currentTest.timeoutStartTime = System.currentTimeMillis();
		currentTest.timeoutEndTime = currentTest.timeoutStartTime + timeoutMillis;
		getCurrentNetworkOperatorRatTestResults().add(currentTest);
		return currentTest;
	}

	public void testRoundComplete(){
		testRoundComplete = true;
		currentTest = null;
	}
	private String escape(String input) {
		if(input == null)
			return "undefined";
		return "\"" + input.replace("\\","\\\\").replace("\"", "\\\"").replace("\n","\\n") + "\"";
	}
	@SuppressLint("DefaultLocale")
	public String getUpdateJavascript(){
		StringBuffer result = new StringBuffer();
		NetworkOperatorTestResults currentNetworkOperator = getCurrentNetworkOperator();
		String currentOperatorName = "Not connected";
		if(currentNetworkOperator != null)
			currentOperatorName = currentNetworkOperator.operatorName;
		result.append("setNetworkOperatorName(" + escape(currentOperatorName) + ");\n");
		if(currentNetworkOperator != null){
			if(currentNetworkOperator.currentGeneration == 2){
				result.append("setGsmActive();\n");
			} else{
				result.append("set3GActive();\n");			
			}
		}
		String mode = (onlineMode ? "Setting: Online, " : "Setting: Offline, ") + numIterations + " x 4 tests";
		result.append("setTestMode(" + escape(mode) + ");\n");
		if(currentNetworkOperator != null){
			result.append("updateBuckets({");
			for(int generation = 2;generation <= 3; generation++){
				String prefix = generation == 2 ? "gsm_" : "3g_";
				for(TestType test:TestType.values()){
					String bucketPrefix = prefix + test.name().toLowerCase();
					result.append("\"" + bucketPrefix + "_success\":" + currentNetworkOperator.getGeneration(generation).getNumSuccess(test) + ",");
					result.append("\"" + bucketPrefix + "_fail\":" + currentNetworkOperator.getGeneration(generation).getNumFailures(test) + ",");	
				}
			}
			result.deleteCharAt(result.length()-1); // Remove final ","
			result.append("});\n"); // End of updateBuckets line
		}
		if(isTestRunning()){
			result.append("setProgressPercent(" + getCurrentTest().getProgressPercent() + ");\n");
			String currentTest = (getCurrentNetworkOperatorRatTestResults().generation == 2 ? "gsm_" : "3g_") + getCurrentTest().type.name().toLowerCase();
			result.append("setCurrentTest(" + escape(currentTest) + ");\n");
		} else{
			result.append("setCurrentTest(\"\");\n");
			result.append("hideProgress();\n");
		}
		String stateMsg;
		if(fatalError != null)
			stateMsg = "Test terminated with fatal error: " + fatalError;
		else
			stateMsg = getCurrentActionString();
		result.append("setStateView(" + escape(stateMsg) + ");\n");
		result.append("setErrorLog(" + escape(getErrorLog()) + ");\n");
		return result.toString();
	}
	public String formatTextTable(){
		StringBuffer result = new StringBuffer();
		NetworkOperatorTestResults currentProvider = getCurrentNetworkOperator();
		if(currentProvider != null){
			currentProvider.formatTable(result);
			// When the phone switches to another provider during the test, there may be more results in this object.
			for(NetworkOperatorTestResults provider:networkOperators.values()){
				if(!provider.mccMnc.equals(currentProvider.mccMnc)){
					result.append("OTHER: " + provider.mccMnc);
					provider.formatTable(result);
				}
			}
		}
		if(isTestRunning()){
			result.append("Progress: " + getCurrentTest().getProgressPercent() + "%  ");
		}
		if(fatalError != null)
			result.append("Test terminated with fatal error: " + fatalError + "\n");
		else
			result.append(getCurrentActionString() + "\n");
		result.append(errorLog);
		return result.toString();
	}
	public String getCurrentActionString(){
		if(blacklisted){
			return "BANNED. Mail to snoopsnitch@srlabs.de.";
		} if(testRoundComplete){
			return "Test round complete. Please also test other network technologies.";
		} else if(getCurrentTest() != null && getCurrentTest().isRunning()){
			return getCurrentTest().getStateDisplayText();
		} else{
			return ""; // No action running
		}
	}
	public boolean isTestRunning(){
		return (getCurrentTest() != null && getCurrentTest().isRunning());
	}
	public int getCurrentRunNumTotal(){
		return 4*numIterations;
	}

	/**
	 * Number of successful tests within the current run. The success percentage
	 * is getCurrentRunNumSuccess()/getCurrentRunNumTotal()
	 * 
	 * @return
	 */
	public int getCurrentRunNumSuccess(){
		return 0;
	}
	/**
	 * Number of failed tests within the current run. The failed percentage
	 * is getCurrentRunNumFailed()/getCurrentRunNumTotal()
	 * 
	 * @return
	 */
	public int getCurrentRunNumFailed(){
		return 0;
	}
	public boolean isTestTypeCompleted(TestType type){
		NetworkOperatorRatTestResults results = getCurrentNetworkOperatorRatTestResults();
		if(results == null)
			return false;
		return (results.getNumSuccess(type) + results.getNumFailures(type) >= numIterations);
	}
	public boolean isTestRoundCompleted(){
		if(!isTestTypeCompleted(TestType.SMS_MO))
			return false;
		if(!isTestTypeCompleted(TestType.CALL_MO))
			return false;
		if(!isTestTypeCompleted(TestType.SMS_MT))
			return false;
		if(!isTestTypeCompleted(TestType.CALL_MT))
			return false;
		return true;
	}
	public void setFatalError(String msg) {
		this.fatalError = msg;
		currentTest = null;
	}
	public String getFatalError() {
		return fatalError;
	}
	public int getNumIterations() {
		return numIterations;
	}
	public void setNumIterations(int numIterations){
		this.numIterations = numIterations;
	}
	public void appendErrorLog(String logMsg){
		SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
		Date date = new Date(System.currentTimeMillis());
		String timestampStr = dateFormat.format(date);
		Utils.formatTimestamp(0);
		MsdLog.i("msd-active-test-service","STATE_INFO: " + logMsg);
		errorLog += timestampStr + "  " + logMsg + "\n";
	}
	void clearCurrentResults(){
		getCurrentNetworkOperatorRatTestResults().clearFails();
	}
	void clearCurrentFails(){
		getCurrentNetworkOperatorRatTestResults().clearResults();		
	}
	public void setOnlineMode(boolean onlineMode) {
		this.onlineMode = onlineMode;
	}
	public String getErrorLog() {
		return errorLog;
	}
	public void clearErrorLog(){
		errorLog = "";
	}
	public boolean isTestTypeContinuable(TestType type){
		NetworkOperatorRatTestResults results = getCurrentNetworkOperatorRatTestResults();
		if(results == null)
			return false;
		return results.getNumSuccess(type) > 0 && results.getNumSuccess(type) < numIterations;
	}
	public boolean isTestRoundContinueable() {
		if(blacklisted)
			return false;
		if(isTestTypeContinuable(TestType.SMS_MO))
			return true;
		if(isTestTypeContinuable(TestType.CALL_MO))
			return true;
		if(isTestTypeContinuable(TestType.SMS_MT))
			return true;
		if(isTestTypeContinuable(TestType.CALL_MT))
			return true;
		return false;
	}
	public void setBlacklisted(boolean b) {
		this.blacklisted = b;
	}
	public boolean isOnlineMode() {
		return onlineMode;
	}
}
