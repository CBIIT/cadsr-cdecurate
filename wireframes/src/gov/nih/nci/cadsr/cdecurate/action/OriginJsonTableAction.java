/*L
 * Copyright ScenPro Inc, SAIC-F
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cadsr-cdecurate/LICENSE.txt for details.
 */

package gov.nih.nci.cadsr.cdecurate.action;

import java.util.Arrays;
import java.util.List;

import com.opensymphony.xwork2.ActionSupport;

public class OriginJsonTableAction extends ActionSupport {
	/**
	 * 
	 */
	private static final long serialVersionUID = 199189436073432781L;
	private List<String> origins;

	public String execute() {
		String[] originArray=new String[]{
				"10 PROMIS Global Based:Based on 10 PROMIS Global Health Questionnaire",
				"10 PROMIS� Global Health:Patient Reported Outcomes Measurement Information System Ten Question Global Health Questionnaire",
				"2005 NHIS QADULT:2005 National Health Interview Survey Questionnaire Sample Adult",
				"ABTC: Adult Brain Tumor Consortium",
				"ACOSOG CRF:American College of Surgeons Oncology Group Case Report Form",
				"ACR BI-RADS(R):American College of Radiology Breast Imaging Reporting and Data System",
				"ACRIN:American College of Radiology Imaging Network",
				"ACS:American Community Survey 2008 Questionnaire",
				"ACTG TIS Classification:AIDS Clinical Trials Group staging classification for AIDS-associated Kaposi's sarcoma",
				"ADEERS:NCI/CTEP Adverse Event Expedited Report System",
				"AJCC Based:Staging Criteria Based on American Joint Committee on Cancer System",
				"AJCC:American Joint Committee on Cancer Cancer Staging Manual, 6th Ed., 2002",
				"AJCC:American Joint Committee on Cancer Cancer Staging Manual, 7th Ed., 2009",
				"ALSPAC:Avon Longitudinal Study of Parents and Children Protocol",
				"AMC CRF:AIDS Malignancy Consortium Case Report Form",
				"Anthropometric Standardization Reference Manual",
				"ARIC Protocol 11; 4/16/1987",
				"ASA24:Automated Self-Administered 24-hour Dietary Recall, NCI",
				"ASC Based: Based on Assessment of Survivor Concerns",
				"ASC:Assessment of Survivor Concerns Gotay CC & Pagano IS. Health Qual Life Outcomes. 2007 Mar 13;5:15",
				"ASCO:American Society of Clinical Oncology",
				"AUA:American Urological Association",
				"BCTOS Based:Based on Breast Cancer Treatment Outcomes Scale",
				"BCTOS:Breast Cancer Treatment Outcomes Scale Questionnaire. Stanton AL, Krishnan L, Collins CA: Cancer 91:2273-81, 2001.",
				"BIS Based:Based on Body Image Scale",
				"BIS:Body Image Scale Hopwood P, Fletcher I, Lee A, et al: A body image scale for use with cancer patients. European Journal of Cancer 37:189-197, 2001.",
				"Bladder CDE Disease Committee",
				"Brain CDE Disease Committee",
				"Breast CDE Disease Committee",
				"caBIG:Cancer Biomedical Informatics Grid",
				"CALGB CRF:Cancer and Leukemia Group B Case Report Form",
				"Cancer Family Registry Data Committee",
				"CAP PROTOCOLS:College of American Pathologists Cancer Protocols",
				"CAP:College of American Pathologists",
				"CARDIA:Coronary Artery Risk Development in Young Adults Study",
				"CCR:Center for Cancer Research",
				"CDC:Center for Disease Control",
				"CDISC:Clinical Data Interchange Standards Consortium",
				"CDS:Clinical Data System",
				"CDUS:Clinical Data Update System",
				"CFR:Code of Federal Regulations",
				"CHIS:California Health Interview Survey 2005 Adult Questionnaire",
				"CHS:Cardiovascular Health Study",
				"CHTN:Cooperative Human Tissue Network",
				"CIA:The World Factbook Cross-Reference List of Country Data Codes",
				"COC:Commission on Cancer",
				"COG CRF:Children's Oncology Group Case Report Form",
				"Colorectal CDE Disease Committee",
				"Confirmation of Molecular Signatures of Early Lung Adenocarcinoma",
				"Cooperative Group-Forms Committee",
				"CROWN CRF:Community Research for Oral Wellness Network Case Report Form",
				"CTCAE:Common Terminology Criteria for Adverse Events v3.0",
				"CTCAEv4.0:Common Terminology Criteria for Adverse Events v4.0",
				"CTEP CDE Committee",
				"CTEP:Cancer Therapy Evaluation Program",
				"CTEP:Phase II Breast CDE Disease Committee",
				"CTMS:caBIG Clinical Trials Management Systems Workspace",
				"CTMS:Clinical Trials Monitoring Service",
				"DCP:Division of Cancer Prevention",
				"DEQAS:Vitamin D Extenral Quality Assessment Scheme htp://www.deqas.org/",
				"DICOM:Digital Imaging and Communications in Medicine",
				"Director's Challenge Lung Study",
				"DPBRN CRF:Dental Practice-Based Research Network Case Report Form",
				"ECOG CRF:Eastern Cooperative Oncology Group Case Report Form",
				"EDRN:Early Detection Research Network",
				"ELCAP:Early Lung Cancer Action Program",
				"eMERGE:Electronic Medical Records and Genomics Network",
				"EORTC QLQ-Pan26: EORTC Quality of Life Questionnaire Pancreas 26",
				"EPIC:Expanded Prostate Cancer Index Composite",
				"EPP CRF:Expanded Participation Project Case Report Form",
				"ePRO:ePRO Data Dictionary",
				"EQ-5D-5L:EuroQol Group EQ-5D Health Questionnaire",
				"FAB Classification",
				"FACIT: FACIT Fatigue Scale Version 4",
				"FACT-G Based:Based on Functional Assessment of Cancer Therapy - General",
				"FACT-G:Functional Assessment of Cancer Therapy - General",
				"FACT:Functional Assessment of Cancer Therapy Quality of Life Scale",
				"Fagerstrom Test",
				"FAHI:Functional Assessment of Human Immunodeficiency Virus Infection",
				"FDA:Food and Drug Administration",
				"FIGO:International Federal of Gynecology and Obstetrics",
				"FIGO:International Federation of Gynecology and Obstretrics Endometrial Staging 2009",
				"FIREBIRD:Federal Investigator Registry of Biomedical Informatics Research Data",
				"Framingham Heart Study",
				"Fred Hutchinson Cancer Research Center, Caffeine Questionnaire, 2004",
				"French System of Histology",
				"G-A100",
				"GBC:Group Banking Committee",
				"Gilly Classification: Gilly Classification published by Gilly et al., 1994, for the characterization of Peritoneal Carcinomatosis",
				"Global Network for Women's and Children's Health Research",
				"GO:Gene Ontology",
				"GOG CRF:Gynecologic Oncology Group Case Report Form",
				"Grooved Pegboard Test, Lezak, 1995",
				"Gynecologic CDE Disease Committee",
				"HAQ-II:Health Assessment Questionnaire-II",
				"HEI:Health Eating Index-2005",
				"HES:Health Examination Survey",
				"HITSP:Healthcare Information Technology Panel",
				"HL7:Health Level Seven",
				"HOW:Health of Women (HOW) Study 11.16.2009",
				"HVLT-R:Hopkins Verbal Learning Test-Revised, Jason Brandt, PhD, 1991",
				"IBCSG CRF:International Breast Cancer Study Group Case Report Form",
				"IBMTR:International Bone Marrow Transplant Registry",
				"ICD:International Classification of Diseases",
				"ICH:International Conference on Harmonization",
				"ICR:caBIG Integrative Cancer Research Workspace",
				"IMT:International Medical Terminology",
				"INFORMED CONSENT",
				"ISA-TAB-Nano:Investigation Study Assay Excel Table Format for Nanotechnology",
				"ISCN Karyotype Description",
				"ISO:International Organization for Standardization",
				"IVI:caBIG In Vivo Imaging Workspace",
				"LCBCC:Lung Cancer Biomarkers and Chemoprevention Consortium",
				"Leukemia CDE Disease Committee",
				"LIDC:Lung Imaging Database Consortium",
				"LOINC:Logical Observation Identifiers Numbers and Codes",
				"Lung CDE Disease Committee",
				"Lymphoma CDE Disease Committee",
				"M-09030",
				"MBSRQ-Appearance Based:Based on Multidimensional Body Self-Relations Questionnaire-Appearance Scale",
				"MBSRQ-Appearance Orientation:Multidimensional Body Self-Relations Questionnaire-Appearance Scale, Brown, TA., Cash, TF, Mikulka, PJ. J Pers Assess. 1990; 55: 135-44.",
				"MDASI-HN:M.D. Anderson Symptom Inventory - Head & Neck, copyright 2000",
				"Measure Instrument:Based on NCI requirement to capture text response for question item.",
				"MedDRA v12.0",
				"MedDRA:Medical Dictionary for Regulatory Affairs",
				"Melanoma CDE Disease Committee",
				"MGED Ontology:Microarray Gene Expression Data Ontology",
				"MGED:Microarray Gene Expression Data",
				"MMQL: Minneapolis-Manchester QOL Survey of Health",
				"MPQ:McGill Pain Questionnaire Short Form, Melzack R, 1987.",
				"Murphy Stage",
				"Myeloma CDE Disease Committee",
				"NAACCR:North American Association of Central Cancer Registries",
				"NCCCP:NCI Community Cancer Centers Program",
				"NCCTG CRF:North Central Cancer Treatment Group Case Report Form",
				"NCI Net-Trials",
				"NCI Thesaurus",
				"NCIC CRF:National Cancer Institute of Canada Case Report Form",
				"NDC:National Drug Code",
				"NDSR:Nutrition Data System for Research, University of Minnesota",
				"NHANES:National Health and Nutrition Examination Survey 2003-2004",
				"NHANES:National Health and Nutrition Examination Survey 2005-2006",
				"NHANES:National Health and Nutrition Examination Survey 2005-2006, Health Insurance Section",
				"NHANES:National Health and Nutrition Examination Survey 2005-2006, Screener Module 1",
				"NHANES:National Health and Nutrition Examination Survey 2007-2008",
				"NHANES:National Health and Nutrition Examination Survey 3, 1988-1994",
				"NHANES:National Health Examination Survey, CDC",
				"NHIS:National Health Interview Survey Family Questionnaire, 2007",
				"NHIS:National Health Interview Survey, 2005",
				"NHLBI FBPP:Family Blood Pressure Program",
				"NINDS:National Institute of Neurological Disorders and Stroke Common Data Element Project",
				"NMDP:National Marrow Donor Program",
				"NOVARTIS:Novartis",
				"NSABP CRF:National Surgical Adjuvant Breast and Bowel Project Case Report Form",
				"NVSS:National Vital Statistics System - CDC",
				"OHIP:Oral Health Impact Profile",
				"OMB/NCI:Office of Management and Budget/National Cancer Institute",
				"OMRS:Oral Mucositis Rating Scale",
				"Pathology CDE Disease Committee",
				"PCF:Prostate Cancer Foundation",
				"PCM:Patient Care Monitor",
				"PEARL CRF:Practitioners Engaged in Applied Research and Learning Network Case Report Form",
				"PedsQL: Pediatric Quality of Life, James W. Varni, Ph.D., 1998",
				"Phase II Lung CDE Disease Committee",
				"POM:Pain-O-Meter",
				"PRAMS:Pregnancy Risk Assessment Monitoring System, 2004-2008",
				"PRECEDENT CRF:Practice-based Research Collaborative in Evidence-Based Dentistry Case Report Form",
				"Prostate CDE Disease Committee",
				"PSID:Panel Study of Income Dynamics, 2007",
				"PSS:Perceived Stress Scale",
				"QLAC:Quality of Life in Adult Cancer Survivors Scale",
				"Radiology Committee",
				"Radiology Committee, UMLS- C0439200",
				"RAI Staging",
				"REAL Classification",
				"RECIST:Response Evaluation Criteria in Solid Tumors",
				"Reference guideline for Solid Tumor Disease: J National Cancer Institute 92(3):205-216, 2000.",
				"RTOG CRF:Radiation Therapy Oncology Group Case Report Form",
				"Sarcoma CDE Disease Committee",
				"SDPS:San Diego Population Study",
				"SEER:Surveillance Epidemiology and End Results, NCI",
				"SHIM:Sexual Health Inventory for Men (SHIM)",
				"SIC",
				"Sipes et al, NEUROLOGY 34;1984",
				"Skindex-29 Copyright 1996, M.-M. Chren, MD",
				"SNOMED:Systematized Nomenclature of Medicine",
				"SNS:Subjective Numeracy Scale",
				"SPOREs:Scientific Programs of Research Excellence",
				"Stunkard, A; et al. Use of the Danish Adoption Registry for the study of obesity and thinness, 1983, 115-120.",
				"SURE-QX:Supplement Reporting Questionnaire",
				"SWOG CRF:Southwest Oncology Group Case Report Form",
				"Tanner Scale/Stage:Tanner JM. Growth at adolescence. 2d ed. Oxford: Blackwell, 1962.",
				"TBPT:caBIG Tissue Banks and Pathology Tools Workspace",
				"TCGA:The Cancer Genome Atlas",
				"THERADEX:Theradex Systems Inc",
				"Trail Making Test by US Army, 1944 as part of the Army Individual Test of General Ability",
				"Transplant CDE Committee",
				"UMLS:Unified Medical Language System, NLM",
				"Upper Gastrointestinal CDE Disease Committee",
				"UWD_VA:University of Washington Visual Anatomist",
				"VA NDFRT:Veterans Administration National Drug File Reference Terminology",
				"VASARI:Visually AccesSAble Rembrandt Images",
				"VCDE:caBIG Vocabulary and Common Data Elements Workspace",
				"VRS:Vienna Rectoscopy Score",
				"WHI:Women's Health Initiative",
				"WHO:World Health Organization",
				"WHODD:World Health Organization Drug Dictionary",
				"Zebrafish "};

		origins = Arrays.asList(originArray);
		return SUCCESS;
	}

	public List<String> getOrigins() {
		return origins;
	}
}
