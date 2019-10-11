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
		this.initialize( "monodirectional/GeezType.txt", "GeezType" );
		
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



	public String convertText( String text, String fontIn ) {
		xlit = fontToTransliteratorMap.get( fontIn );
		if ( xlit == null ) {
			return null;
		}
		
		StringBuilder sb = new StringBuilder();
		
		for(int i = 0; i < text.length(); i++) {
			int x = ( 0x00ff & (int)text.charAt(i) );
			sb.append( (char)x );
		}
		
		return xlit.transliterate( sb.toString() );
	}
	
	
	public boolean isSpacePreservableSymbol(String space) {
		return ( space.equals("\uf020") );
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
