package com.permabound.simplescraper;

import org.jsoup.nodes.Document;


public class SimpleJSoupPageProcessor extends AbstractJSoupPageProcessor {

	@Override
	protected void handle( PageUrl url, int status, Document soup ) {
		
		if ( status != 200 )
			System.err.println( url.getUrl( ) + " from " + url.getReferringUrl( ) + " " + status );
		
		else if ( soup.text( ).contains( "No results" ) )
			System.err.println( url.getUrl( ) + " from " + url.getReferringUrl( ) + " no search results" );
	}


}
