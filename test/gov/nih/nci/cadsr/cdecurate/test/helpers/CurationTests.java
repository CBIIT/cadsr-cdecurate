package gov.nih.nci.cadsr.cdecurate.test.helpers;

public class CurationTests {
public static void main(String[] args) {
	String sDataType = "NUMBER";
	boolean existed = (!sDataType.equalsIgnoreCase("NUMBER"));
	System.out.println(existed);
	boolean disable = ((!sDataType.equalsIgnoreCase("Integer")) && (!sDataType.equalsIgnoreCase("NUMBER")));
	System.out.println(disable);

}
}
