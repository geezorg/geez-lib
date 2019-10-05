package org.geez.convert;

import org.geez.convert.helpers.ICUHelper;

import com.ibm.icu.lang.UCharacter;
import com.ibm.icu.text.BreakIterator;
import com.ibm.icu.text.Transliterator;

/**
* <code>Converter</code> is an abstract class to be used for defining special case converters
* most commonly for legacy Ethiopic font encodings systems (which may be diacritical based or
* multi-part systems).  The class provides methods for reading ICU transliterations defined in
* either LDML or plain text files.
* 
* @author  Daniel Yacob
* @version 0.1.0
* @since   2019-09-17 
*/
public abstract class Converter extends ICUHelper {
	protected char huletNeteb = 0x0;
	public Transliterator xlit = null;
	protected int icuDirection = -1;
	public static String[] IDs = null;

    public Converter() {
    }
       
    public Converter( String direction ) {
    	setDirection( direction );
    }
    
    public Converter( int  icuDirection ) {
    	setDirection( icuDirection );
    }
    
    public void setDirection( String direction ) {
		this.icuDirection = ( direction.equals("both") || direction.equals("forward") ) ? Transliterator.FORWARD : Transliterator.REVERSE;	
    }
    
    public void setDirection( int icuDirection ) {
		this.icuDirection = icuDirection; // should throw an exception if not Transliterator.FORWARD or Transliterator.REVERSE;
    }
    
    public String[] getIDs() {
    	return IDs;
    }
	
	public boolean isSpacePreservableSymbol(String space) {
		return false;
	}
	
	
	protected String caseOption = null;
	public void setCaseOption(String caseOption ) {
		// there should be an enumeration for case options
		this.caseOption = caseOption;
	}
	
	public String remapCase(String textIn) {
		
		String textOut = null;
		switch( (caseOption==null) ? "" : caseOption ) {
			case "lowercase": 
				textOut = UCharacter.toLowerCase( textIn );
				break;
				
			case "uppercase": 
				textOut = UCharacter.toUpperCase( textIn );
				break;
				
			case "title case": 
				textOut = UCharacter.toTitleCase( textIn, BreakIterator.getWordInstance() );
				break;
				
			default:
				textOut = textIn;
				break;
			
		}
		return textOut;
	}

}
