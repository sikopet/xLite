/*
 * Generated by asn1c-0.9.24 (http://lionet.info/asn1c)
 * From ASN.1 module "InformationElements"
 * 	found in "../asn/InformationElements.asn"
 * 	`asn1c -fcompound-names -fnative-types`
 */

#ifndef	_ExtGANSS_SIBTypeInfoSchedulingInfo_H_
#define	_ExtGANSS_SIBTypeInfoSchedulingInfo_H_


#include <asn_application.h>

/* Including external dependencies */
#include <NativeInteger.h>
#include "UE-Positioning-GANSS-SBAS-ID.h"
#include <asn_SEQUENCE_OF.h>
#include <constr_SEQUENCE_OF.h>
#include <constr_SEQUENCE.h>

#ifdef __cplusplus
extern "C" {
#endif

/* Forward declarations */
struct ExtGANSS_SchedulingInfo;

/* ExtGANSS-SIBTypeInfoSchedulingInfo */
typedef struct ExtGANSS_SIBTypeInfoSchedulingInfo {
	long	*ganssID	/* OPTIONAL */;
	UE_Positioning_GANSS_SBAS_ID_t	*sbasID	/* OPTIONAL */;
	struct ExtGANSS_SIBTypeInfoSchedulingInfo__ganssScheduling {
		A_SEQUENCE_OF(struct ExtGANSS_SchedulingInfo) list;
		
		/* Context for parsing across buffer boundaries */
		asn_struct_ctx_t _asn_ctx;
	} ganssScheduling;
	
	/* Context for parsing across buffer boundaries */
	asn_struct_ctx_t _asn_ctx;
} ExtGANSS_SIBTypeInfoSchedulingInfo_t;

/* Implementation */
extern asn_TYPE_descriptor_t asn_DEF_ExtGANSS_SIBTypeInfoSchedulingInfo;

#ifdef __cplusplus
}
#endif

/* Referred external types */
#include "ExtGANSS-SchedulingInfo.h"

#endif	/* _ExtGANSS_SIBTypeInfoSchedulingInfo_H_ */
#include <asn_internal.h>