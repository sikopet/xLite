/*
 * Generated by asn1c-0.9.24 (http://lionet.info/asn1c)
 * From ASN.1 module "InformationElements"
 * 	found in "../asn/InformationElements.asn"
 * 	`asn1c -fcompound-names -fnative-types`
 */

#include "GSM-Measurements.h"

static asn_TYPE_member_t asn_MBR_GSM_Measurements_1[] = {
	{ ATF_NOFLAGS, 0, offsetof(struct GSM_Measurements, gsm900),
		(ASN_TAG_CLASS_CONTEXT | (0 << 2)),
		-1,	/* IMPLICIT tag at current level */
		&asn_DEF_BOOLEAN,
		0,	/* Defer constraints checking to the member type */
		0,	/* No PER visible constraints */
		0,
		"gsm900"
		},
	{ ATF_NOFLAGS, 0, offsetof(struct GSM_Measurements, dcs1800),
		(ASN_TAG_CLASS_CONTEXT | (1 << 2)),
		-1,	/* IMPLICIT tag at current level */
		&asn_DEF_BOOLEAN,
		0,	/* Defer constraints checking to the member type */
		0,	/* No PER visible constraints */
		0,
		"dcs1800"
		},
	{ ATF_NOFLAGS, 0, offsetof(struct GSM_Measurements, gsm1900),
		(ASN_TAG_CLASS_CONTEXT | (2 << 2)),
		-1,	/* IMPLICIT tag at current level */
		&asn_DEF_BOOLEAN,
		0,	/* Defer constraints checking to the member type */
		0,	/* No PER visible constraints */
		0,
		"gsm1900"
		},
};
static ber_tlv_tag_t asn_DEF_GSM_Measurements_tags_1[] = {
	(ASN_TAG_CLASS_UNIVERSAL | (16 << 2))
};
static asn_TYPE_tag2member_t asn_MAP_GSM_Measurements_tag2el_1[] = {
    { (ASN_TAG_CLASS_CONTEXT | (0 << 2)), 0, 0, 0 }, /* gsm900 at 1250 */
    { (ASN_TAG_CLASS_CONTEXT | (1 << 2)), 1, 0, 0 }, /* dcs1800 at 1251 */
    { (ASN_TAG_CLASS_CONTEXT | (2 << 2)), 2, 0, 0 } /* gsm1900 at 1252 */
};
static asn_SEQUENCE_specifics_t asn_SPC_GSM_Measurements_specs_1 = {
	sizeof(struct GSM_Measurements),
	offsetof(struct GSM_Measurements, _asn_ctx),
	asn_MAP_GSM_Measurements_tag2el_1,
	3,	/* Count of tags in the map */
	0, 0, 0,	/* Optional elements (not needed) */
	-1,	/* Start extensions */
	-1	/* Stop extensions */
};
asn_TYPE_descriptor_t asn_DEF_GSM_Measurements = {
	"GSM-Measurements",
	"GSM-Measurements",
	SEQUENCE_free,
	SEQUENCE_print,
	SEQUENCE_constraint,
	SEQUENCE_decode_ber,
	SEQUENCE_encode_der,
	SEQUENCE_decode_xer,
	SEQUENCE_encode_xer,
	SEQUENCE_decode_uper,
	SEQUENCE_encode_uper,
	0,	/* Use generic outmost tag fetcher */
	asn_DEF_GSM_Measurements_tags_1,
	sizeof(asn_DEF_GSM_Measurements_tags_1)
		/sizeof(asn_DEF_GSM_Measurements_tags_1[0]), /* 1 */
	asn_DEF_GSM_Measurements_tags_1,	/* Same as above */
	sizeof(asn_DEF_GSM_Measurements_tags_1)
		/sizeof(asn_DEF_GSM_Measurements_tags_1[0]), /* 1 */
	0,	/* No PER visible constraints */
	asn_MBR_GSM_Measurements_1,
	3,	/* Elements count */
	&asn_SPC_GSM_Measurements_specs_1	/* Additional specs */
};
