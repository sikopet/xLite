/*
 * Generated by asn1c-0.9.24 (http://lionet.info/asn1c)
 * From ASN.1 module "InformationElements"
 * 	found in "../asn/InformationElements.asn"
 * 	`asn1c -fcompound-names -fnative-types`
 */

#include "PDSCH-SysInfo.h"

static asn_TYPE_member_t asn_MBR_PDSCH_SysInfo_1[] = {
	{ ATF_NOFLAGS, 0, offsetof(struct PDSCH_SysInfo, pdsch_Identity),
		(ASN_TAG_CLASS_CONTEXT | (0 << 2)),
		-1,	/* IMPLICIT tag at current level */
		&asn_DEF_PDSCH_Identity,
		0,	/* Defer constraints checking to the member type */
		0,	/* No PER visible constraints */
		0,
		"pdsch-Identity"
		},
	{ ATF_NOFLAGS, 0, offsetof(struct PDSCH_SysInfo, pdsch_Info),
		(ASN_TAG_CLASS_CONTEXT | (1 << 2)),
		-1,	/* IMPLICIT tag at current level */
		&asn_DEF_PDSCH_Info,
		0,	/* Defer constraints checking to the member type */
		0,	/* No PER visible constraints */
		0,
		"pdsch-Info"
		},
	{ ATF_POINTER, 2, offsetof(struct PDSCH_SysInfo, dsch_TFS),
		(ASN_TAG_CLASS_CONTEXT | (2 << 2)),
		+1,	/* EXPLICIT tag at current level */
		&asn_DEF_TransportFormatSet,
		0,	/* Defer constraints checking to the member type */
		0,	/* No PER visible constraints */
		0,
		"dsch-TFS"
		},
	{ ATF_POINTER, 1, offsetof(struct PDSCH_SysInfo, dsch_TFCS),
		(ASN_TAG_CLASS_CONTEXT | (3 << 2)),
		+1,	/* EXPLICIT tag at current level */
		&asn_DEF_TFCS,
		0,	/* Defer constraints checking to the member type */
		0,	/* No PER visible constraints */
		0,
		"dsch-TFCS"
		},
};
static int asn_MAP_PDSCH_SysInfo_oms_1[] = { 2, 3 };
static ber_tlv_tag_t asn_DEF_PDSCH_SysInfo_tags_1[] = {
	(ASN_TAG_CLASS_UNIVERSAL | (16 << 2))
};
static asn_TYPE_tag2member_t asn_MAP_PDSCH_SysInfo_tag2el_1[] = {
    { (ASN_TAG_CLASS_CONTEXT | (0 << 2)), 0, 0, 0 }, /* pdsch-Identity at 10033 */
    { (ASN_TAG_CLASS_CONTEXT | (1 << 2)), 1, 0, 0 }, /* pdsch-Info at 10034 */
    { (ASN_TAG_CLASS_CONTEXT | (2 << 2)), 2, 0, 0 }, /* dsch-TFS at 10035 */
    { (ASN_TAG_CLASS_CONTEXT | (3 << 2)), 3, 0, 0 } /* dsch-TFCS at 10036 */
};
static asn_SEQUENCE_specifics_t asn_SPC_PDSCH_SysInfo_specs_1 = {
	sizeof(struct PDSCH_SysInfo),
	offsetof(struct PDSCH_SysInfo, _asn_ctx),
	asn_MAP_PDSCH_SysInfo_tag2el_1,
	4,	/* Count of tags in the map */
	asn_MAP_PDSCH_SysInfo_oms_1,	/* Optional members */
	2, 0,	/* Root/Additions */
	-1,	/* Start extensions */
	-1	/* Stop extensions */
};
asn_TYPE_descriptor_t asn_DEF_PDSCH_SysInfo = {
	"PDSCH-SysInfo",
	"PDSCH-SysInfo",
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
	asn_DEF_PDSCH_SysInfo_tags_1,
	sizeof(asn_DEF_PDSCH_SysInfo_tags_1)
		/sizeof(asn_DEF_PDSCH_SysInfo_tags_1[0]), /* 1 */
	asn_DEF_PDSCH_SysInfo_tags_1,	/* Same as above */
	sizeof(asn_DEF_PDSCH_SysInfo_tags_1)
		/sizeof(asn_DEF_PDSCH_SysInfo_tags_1[0]), /* 1 */
	0,	/* No PER visible constraints */
	asn_MBR_PDSCH_SysInfo_1,
	4,	/* Elements count */
	&asn_SPC_PDSCH_SysInfo_specs_1	/* Additional specs */
};
