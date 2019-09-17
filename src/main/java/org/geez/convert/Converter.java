package org.geez.convert;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.FilenameUtils;
import org.w3c.dom.CharacterData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.ibm.icu.text.Transliterator;

/**
* <h1>Converter</h1>
* This is an abstract class to be used for defining converters
* for specific document formats (DOCX, HTML, TXT) or stream 
* types.
* 
* @author  Daniel Yacob
* @version 0.1.0
* @since   2019-09-17 
*/
public abstract class Converter {
	protected char huletNeteb = 0x0;
	public Transliterator xlit = null;
	protected int icuDirection = -1;
	public static String[] IDs = null;
    
    protected static DocumentBuilder builder = null; 
    {
    	try {
    		builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
    	
    		builder.setEntityResolver(new EntityResolver() {
    			public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
    				if (systemId.contains("ldmlSupplemental.dtd")) {
    					ClassLoader classLoader = this.getClass().getClassLoader();
    					InputStream dtdStream = classLoader.getResourceAsStream( "common/dtd/ldmlSupplemental.dtd" );
    					return new InputSource(dtdStream);
    				} else {
    					return null;
    				}
    			}
    		});
    	}
    	catch( ParserConfigurationException ex) {
    		System.err.println( ex );
    	}
    }

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
	
	private String readRulesFromStream( InputStream is ) throws IOException {
		String line, segment, rules = "";
		BufferedReader rulesFile = new BufferedReader( new InputStreamReader(is, "UTF-8") );
		while ( (line = rulesFile.readLine()) != null) {
			if ( line.trim().equals("") || line.charAt(0) == '#' ) {
				continue;
			}
			segment = line.replaceFirst ( "^(.*?)#(.*)$", "$1" );
			rules += ( segment == null ) ? line : segment;
		}
		rulesFile.close();
		return rules;	
	}
	
    /**
     * Returns a single line string of ICU transliteration rules that may be passed to the
     * ICU <tt>Transliterator.createFromRules</tt> API.
     * 
     * @param rulesFileName the file name (or path) for the file containing ICU transliteration rules.
     * @return a single line String containing the file content
     * @throws IOException if an error occurs reading the specified file
     * @throws SAXException if the XML document is invalid
     * @since 0.1.0
     */
	public String readRulesResourceFile( String rulesFileName ) throws IOException, SAXException {
		if( FilenameUtils.getExtension( rulesFileName ).equals( "xml" ) ) {
			return readRulesResourceFileXML( rulesFileName );
		}
		else {
			return readRulesResourceFileText( rulesFileName );
		}
	}	
	
    /**
     * Given a text input file, returns a single line string of ICU transliteration rules that may
     * be passed to the ICU <tt>Transliterator.createFromRules</tt> API.
     * 
     * @param rulesFileTXT the name (or path) of a text file containing ICU transliteration rules.
     * @return a single line String containing the file content
     * @throws IOException if an error occurs reading the specified file
     * @since 0.1.0
     */
	public String readRulesResourceFileText( String rulesFileTXT ) throws IOException {
		ClassLoader classLoader = this.getClass().getClassLoader();
		InputStream in = classLoader.getResourceAsStream( "tables/" + rulesFileTXT ); 
		return readRulesFromStream( in );	
	}
		
    /**
     * Given an LDML input file, returns a single line string of ICU transliteration rules that may
     * be passed to the ICU <tt>Transliterator.createFromRules</tt> API.
     * 
     * @param rulesFileXML the name (or path) of an XML file containing ICU transliteration rules.
     * @return a single line String containing the file content
     * @throws IOException if an error occurs reading the specified file
     * @throws SAXException if the XML document is invalid
     * @since 0.1.0
     */
	public String readRulesResourceFileXML( String rulesFileXML ) throws IOException, SAXException {		
		ClassLoader classLoader = this.getClass().getClassLoader();
		if(! rulesFileXML.contains( "/" ) ) {
			// this is a week test for a path hierarchy, we assume some path for a user defined file
			rulesFileXML = "common/transforms/" + rulesFileXML ; 
		}
		InputStream xmlStream = classLoader.getResourceAsStream( rulesFileXML );    
	    
	    Document doc = builder.parse( xmlStream );
	    NodeList nodes = doc.getElementsByTagName( "tRule" );
	    Element  element = (Element) nodes.item(0); // assume only one
	    
	    String rulesString = getCharacterDataFromElement( element );
	    InputStream is = new ByteArrayInputStream( rulesString.getBytes() );
	    
		return readRulesFromStream( is );
	}
	
    /**
     * Given an LDML input string, returns a single line string of ICU transliteration rules that may
     * be passed to the ICU <tt>Transliterator.createFromRules</tt> API.
     * 
     * @param rulesStringXML a string representation of an LDML document containng ICU transliteration rules.
     * @return a single line String containing the file content
     * @throws IOException if an error occurs reading the specified file
     * @throws SAXException if the XML document is invalid
     * @since 0.1.0
     */	
	public String readRulesStringXML( String rulesStringXML ) throws IOException, SAXException {		
		InputStream rulesStream = new ByteArrayInputStream(rulesStringXML.getBytes());
	    
	    Document doc = builder.parse( rulesStream );
	    NodeList nodes = doc.getElementsByTagName( "tRule" );
	    Element  element = (Element) nodes.item(0); // assume only one
	    
	    String rulesString = getCharacterDataFromElement( element );
	    InputStream is = new ByteArrayInputStream( rulesString.getBytes() );
	    
		return readRulesFromStream( is );
	}

	private static String getCharacterDataFromElement( Element e ) {
		Node child = e.getFirstChild();
		if (child instanceof CharacterData) {
			CharacterData cd = (CharacterData) child;
			return cd.getData();
		}
		return "";
	}
	
	public boolean isSpacePreservableSymbol(String space) {
		return false;
	}

}
