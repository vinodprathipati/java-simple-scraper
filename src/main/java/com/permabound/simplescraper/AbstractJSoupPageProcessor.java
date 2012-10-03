package com.permabound.simplescraper;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public abstract class AbstractJSoupPageProcessor implements PageProcessor {

	public Collection<PageUrl> process( PageUrl url, int status, byte[ ] payload ) {
		
		List<PageUrl> pageUrls = new ArrayList<PageUrl>( );
		
		try {
			Document soup = Jsoup.parse( new ByteArrayInputStream( payload ), Charset.defaultCharset( ).name( ), url.getUrl( ).toString( ) );
			this.handle( url, status, soup );
			for ( Element e : soup.getElementsByTag( "a" ) ) {
				try {
					URL href = new URL( e.attr( "abs:href" ) );
					if ( "http".equals( href.getProtocol( ) ) || "https".equals( href.getProtocol( ) ) )
						pageUrls.add( new PageUrl( href, url.getUrl( ) ) );
				} catch( Exception ee ) {
					//ignore invalid URLs
				}
			}
		} catch( IOException e ) {
			e.printStackTrace( );
		}
		
		return pageUrls;
	}
	
	protected abstract void handle( PageUrl url, int status, Document soup );
	
}
