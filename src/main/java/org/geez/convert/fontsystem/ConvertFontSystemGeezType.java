package org.geez.convert.fontsystem;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;



public class ConvertFontSystemGeezType extends ConvertFontSystemDiacriticalSystem {

	public static final Set<String> supportedFonts = new HashSet<String> ( Arrays.asList( "GeezType" ) );
	
	{
		IDs = new String[] { "GeezType" } ;
	}
	
	public ConvertFontSystemGeezType() {
		super();
		init();
	}
	
	private void init() {
		this.initialize( "biirectional/GeezType.txt", "GeezType" );
		
		huletNeteb = ':';
		
		diacritics.addAll (
			Arrays.asList( 
					"\u00e8", "\u00e9", "\u00ea", "\u00eb", "\u00ec", "\u00ed", "\u00ee", "\u00ef",
					"\u00f0", "\u00f1", "\u00f2", "\u00f3", "\u00f4", "\u00f5", "\u00f6", "\u00f7", 
					"\u00f8", "\u00f9", "\u00fa", "\u00fb", "\u00fc", "\u00fd", "\u00fe", "\u00ff"
			)
		);
		
		translit2 = null;
		
		buildRE();
	}

	
	/*
	public boolean isMacron(String text) {
		if ( text.equals( "\u007a" ) ) {
			return true;
		}
		return false;
	}
	*/
	
}
