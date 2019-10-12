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
					"\uf0e8", "\uf0e9", "\uf0ea", "\uf0eb", "\uf0ec", "\uf0ed", "\uf0ee", "\uf0ef",
					"\uf0f0", "\uf0f1", "\uf0f2", "\uf0f3", "\uf0f4", "\uf0f5", "\uf0f6", "\uf0f7", 
					"\uf0f8", "\uf0f9", "\uf0fa", "\uf0fb", "\uf0fc", "\uf0fd", "\uf0fe", "\uf0ff"
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
