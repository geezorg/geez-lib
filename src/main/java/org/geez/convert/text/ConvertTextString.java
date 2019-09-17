package org.geez.convert.text;

import java.io.IOException;
import java.util.UUID;

import org.geez.convert.Converter;
import org.xml.sax.SAXException;

import com.ibm.icu.text.Transliterator;



public class ConvertTextString extends Converter {
	private String textIn = null;
	private String textOut = null;

	
    public ConvertTextString( String tableRulesFileName, String direction ) throws Exception  {
		this.initialize( tableRulesFileName, direction );
    }  

	
    public ConvertTextString( String tableRulesFileName, int icuDirection ) throws Exception  {
		this.initialize( tableRulesFileName, icuDirection );
    }  


    // revisit this, the isEditor argument is a dummy used to force the  creation of
    // new constructor.  "Xliterator-" is also too application-specific.
    // perhaps use an empty constructor and methods to set the other parameters
    // from the Xliterator
    
    // Remove this constructor after refactoring
    public ConvertTextString( String editorText, String direction, boolean isEditor ) throws Exception {	
		String rulesText = editorText;
		if( editorText.startsWith( "<?xml" ) ) {
			rulesText = readRulesStringXML( editorText );
		}
		icuDirection = (direction.equals("both") || direction.equals("forward"))
				 ? Transliterator.FORWARD 
				 : Transliterator.REVERSE // || direction.equals("reverse") 
				 ;
		xlit = Transliterator.createFromRules( "Xliterator-" + UUID.randomUUID(), rulesText, icuDirection );
	}
    
    public ConvertTextString() {
    	
    }
    public void setRules( String rulesText, String direction ) throws SAXException, IOException {
    	String tableRulesText = rulesText;
		if( rulesText.startsWith( "<?xml" ) ) {
			tableRulesText = readRulesStringXML( rulesText );
		}
		icuDirection = (direction.equals("both") || direction.equals("forward"))
				 ? Transliterator.FORWARD 
				 : Transliterator.REVERSE // || direction.equals("reverse") 
				 ;
		xlit = Transliterator.createFromRules( "GeezLib-" + UUID.randomUUID(), tableRulesText, icuDirection );	
    }

    
	void initialize( final String tableRulesFileName, final String direction ) throws Exception {
		icuDirection = (direction.equals("both") || direction.equals("forward"))
					 ? Transliterator.FORWARD 
					 : Transliterator.REVERSE // || direction.equals("reverse") 
					 ;

		initialize( tableRulesFileName, icuDirection );
	}
	    
	void initialize( final String tableRulesFile, final int icuDirection ) throws Exception {
			String id = tableRulesFile; // remove the file extension
			
			String rulesText = this.readRulesResourceFile( tableRulesFile );

			xlit = Transliterator.createFromRules( id, rulesText.replace( '\ufeff', ' ' ), icuDirection );
	}


    public void setText( String textIn ) {
    	this.textIn = textIn;
    }
    
    public String getText() {
    	return textIn;
    }
      
    public String getTextOut() {
    	return textOut;
    }


	public String convert() {
		if( textIn == null )
			return null;
		
		textOut = xlit.transliterate( textIn );
		return textOut;
	}
	public String convertText( String text ) {
		setText( text );
		return convert();
	}


}
