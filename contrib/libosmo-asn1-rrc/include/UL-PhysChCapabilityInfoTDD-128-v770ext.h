/*
 * Generated by asn1c-0.9.24 (http://lionet.info/asn1c)
 * From ASN.1 module "InformationElements"
 * 	found in "../asn/InformationElements.asn"
 * 	`asn1c -fcompound-names -fnative-types`
 */

#ifndef	_UL_PhysChCapabilityInfoTDD_128_v770ext_H_
#define	_UL_PhysChCapabilityInfoTDD_128_v770ext_H_


#include <asn_application.h>

/* Including external dependencies */
#include "MaxPhysChPerTimeslot-LCR-r7.h"
#include <constr_SEQUENCE.h>

#ifdef __cplusplus
extern "C" {
#endif

/* UL-PhysChCapabilityInfoTDD-128-v770ext */
typedef struct UL_PhysChCapabilityInfoTDD_128_v770ext {
	MaxPhysChPerTimeslot_LCR_r7_t	 maxPhysChPerTimeslot;
	
	/* Context for parsing across buffer boundaries */
	asn_struct_ctx_t _asn_ctx;
} UL_PhysChCapabilityInfoTDD_128_v770ext_t;

/* Implementation */
extern asn_TYPE_descriptor_t asn_DEF_UL_PhysChCapabilityInfoTDD_128_v770ext;

#ifdef __cplusplus
}
#endif

#endif	/* _UL_PhysChCapabilityInfoTDD_128_v770ext_H_ */
#include <asn_internal.h>