package org.geez.convert.fontsystem;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;




public class ConvertFontSystemDiacriticalSystem extends ConvertFontSystemDuoFont {
	protected final List<String> font1Typefaces = new ArrayList<String>();

	protected final ArrayList<String> diacritics = new ArrayList<String>();
	protected final ArrayList<String> leftDiacritics = new ArrayList<String>();
	protected Pattern diacriticsRE = null;
	
	
	public ConvertFontSystemDiacriticalSystem() {
		super();
	}
	
	
	protected void buildRE() {
		
		StringBuilder sb = new StringBuilder();
		for (String s : diacritics) {
			sb.append(s);
		}
		
		diacriticsRE = Pattern.compile(
			"([" + sb + "])([" + sb + "])"
		);
		
	}

	
	// reconsider the need for the first argument:
	public boolean isDiacritic(String fontName, char diacritic) {
		/*
		 * this shouldn't happen
		 * 
		if ( diacritic == '\0' ) {
			return false;
		}
		*/
		return diacritics.contains( String.valueOf( diacritic ) );
	}
	

	
	public boolean isLeftDiacritic(String fontName, char diacritic ) {
		return leftDiacritics.contains( String.valueOf( diacritic ) );
	}
	
	
	/* this method appears to no longer be in use

	public boolean isContinuant(String fontName, String text) {
		
		return ( isDiacritic(fontName, text) || (text.charAt(0) == huletNeteb) );
		
	}
	*/

	
	/*
	public String convertText( Text text, String fontIn ) {
		localCheck( text );
		return xlit.transliterate( text.getValue() );
	}
	*/
	
	
	public boolean combinesWithHuletNeteb(char symbol) {
		return ( symbol == huletNeteb );
	}
	

}
