/*
 * Generated by asn1c-0.9.24 (http://lionet.info/asn1c)
 * From ASN.1 module "PDU-definitions"
 * 	found in "../asn/PDU-definitions.asn"
 * 	`asn1c -fcompound-names -fnative-types`
 */

#ifndef	_InterRATHandoverInfo_v690ext1_IEs_H_
#define	_InterRATHandoverInfo_v690ext1_IEs_H_


#include <asn_application.h>

/* Including external dependencies */
#include <constr_SEQUENCE.h>

#ifdef __cplusplus
extern "C" {
#endif

/* Forward declarations */
struct UE_RadioAccessCapability_v650ext;

/* InterRATHandoverInfo-v690ext1-IEs */
typedef struct InterRATHandoverInfo_v690ext1_IEs {
	struct UE_RadioAccessCapability_v650ext	*ue_RadioAccessCapability_v650ext	/* OPTIONAL */;
	
	/* Context for parsing across buffer boundaries */
	asn_struct_ctx_t _asn_ctx;
} InterRATHandoverInfo_v690ext1_IEs_t;

/* Implementation */
extern asn_TYPE_descriptor_t asn_DEF_InterRATHandoverInfo_v690ext1_IEs;

#ifdef __cplusplus
}
#endif

/* Referred external types */
#include "UE-RadioAccessCapability-v650ext.h"

#endif	/* _InterRATHandoverInfo_v690ext1_IEs_H_ */
#include <asn_internal.h>
