module geez.lib {
	exports org.geez.convert.helpers;
	exports org.geez.convert;
	exports org.geez.convert.text;
	exports org.geez.convert.docx;
	exports org.geez.convert.fontsystem;

	requires java.xml;
	requires javafx.base;
	requires com.ibm.icu;
	requires org.docx4j.core;
}