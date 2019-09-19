package org.geez.convert;


/**
* <code>Numeric</code> is a class to be used for defining special case converters
* most commonly for legacy Ethiopic font encodings systems (which may be diacritical based or
* multi-part systems).  The class provides methods for reading ICU transliterations defined in
* either LDML or plain text files.
* 
* @author  Daniel Yacob
* @version 0.1.0
* @since   2019-09-17 
*/
public class Numeric {

	private static final char ETHIOPIC_ONE          = 0x1369;
	private static final char ETHIOPIC_TEN          = 0x1372;
	private static final char ETHIOPIC_HUNDRED      = 0x137B;
	private static final char ETHIOPIC_TEN_THOUSAND = 0x137C;
	
	public static String toEthiopic ( String asciiNumberString ) {
		int n = asciiNumberString.length() - 1;
		
		 if ( (n%2) == 0 ) {
		 	asciiNumberString = "0" + asciiNumberString;
		 	n++;
		 }
		 
		 String ethioNumberString = "";
		 char asciiOne, asciiTen, ethioOne, ethioTen;
	 
		 for ( int place = n; place >= 0; place-- ) {
		 	asciiOne = asciiTen = ethioOne = ethioTen = 0x0;

			asciiTen = asciiNumberString.charAt( n - place );
			place--;
			asciiOne = asciiNumberString.charAt( n - place );
			
			 if ( asciiOne != '0' )
			 	ethioOne = (char)((int)asciiOne + ( ETHIOPIC_ONE - '1' ));
				
			if ( asciiTen != '0' ) 
				ethioTen = (char)((int)asciiTen + ( ETHIOPIC_TEN - '1'));
			
			int pos = ( place % 4 ) / 2;
			
			char sep = ( place != 0 )
			          ? ( pos != 0 )
			             ? ( ( ethioOne != 0x0 ) || ( ethioTen != 0x0 ) )
			                ? ETHIOPIC_HUNDRED
			                :  0x0
			             : ETHIOPIC_TEN_THOUSAND
			          :  0x0
			        ;

			if ( ( ethioOne == ETHIOPIC_ONE ) && ( ethioTen == 0x0 ) && ( n > 1 ) )
			  if ( ( sep == ETHIOPIC_HUNDRED ) || ( (place + 1) == n ) )
			    ethioOne = 0x0;
			    

			if ( ethioTen != 0x0 )
			  ethioNumberString += String.valueOf ( ethioTen );
			if ( ethioOne != 0x0 )
			  ethioNumberString += String.valueOf ( ethioOne );			  
			if ( sep != 0x0 )
			  ethioNumberString += String.valueOf ( sep );				
		 }
		 
		 return ( ethioNumberString );
	}
	

	public boolean isEthiopicNumber(char ethNumber) {
		return ( ('፩' <= (int)ethNumber) && ((int)ethNumber <= '፼') );
	}

	/*
	 *  AB፼AB፻AB፼AB፻AB  =>  AB፻^5 + AB፻^4 + AB፻^3 + AB፻^2 + AB፻^1 + AB፻^0
	 * 
	 */
	public int toInteger (String ethiopic) {

		/* Confirm that we have a valid number string */
		if( (ethiopic == null) || (ethiopic.length() == 0) )
			return ( -1 ); // failure code


		int numberLength = ethiopic.length();
		for ( int i=0; i < numberLength; i++ ) {
			if ( !isEthiopicNumber ( ethiopic.charAt(i) ) ) {
				return ( -1 );
			}
		}


		/*
		 *  Read right to left to get the orders of 10 right
		 */
		int numout=0, power=0;
		int firstNumber = numberLength - 1;
		int A=0, B=0;
		boolean sawMeto = false;
		
		for( int i = firstNumber; i>=0; i-- )
		{
			int X = (int)ethiopic.charAt(i);

			if ( X == '፻' || X == '፼' ) {
				if( X == '፼' ) {
					if( sawMeto ) {
						if ( A == 0 ) {
							A = 1;
						}
						//we may have skipped a B, so add A:
						numout += A*(int)Math.pow((double)100, (double)(power));							
					} else {
						//we may have skipped a B, so add A:
						numout += A*(int)Math.pow((double)100, (double)(power));						
					}
					sawMeto = false;
					power++;
				} else {
					numout += A*(int)Math.pow((double)100, (double)(power));
					power++;
					sawMeto = true;
				}
				
				if( numberLength == 1 ) {
					numout = (int)Math.pow((double)100, (double)power);
					return numout;
				}
				
				if( i == 0 ) {
					numout += (int)Math.pow((double)100, (double)power);
				}
				
				A = 0;
				continue;
			}

			if ( X >= '፲' ) {
				B = (X - '፱') * 10;
				numout += (B + A) * (int)Math.pow((double)100, (double)power);
				A = B = 0;
				sawMeto = false;
			}
			else {
				A = (X - '፩') + 1;
				if( i == 0 ) {
					numout +=  A * (int)Math.pow((double)100, (double)power);
				}
			}
		}

		return numout ;

	}
}
