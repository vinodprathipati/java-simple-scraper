package com.permabound.simplescraper;

import java.net.URI;
import java.net.URL;
import java.util.Set;
import java.util.concurrent.BlockingQueue;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;

public class ScraperThread extends Thread {
	
	private HttpClient client;
	
	private BlockingQueue<PageUrl> urlQueue;
	
	private Set<URL> visitedUrls;
	
	private PageProcessor processor;
	
	private UrlMatcher matcher;
	
	public ScraperThread( HttpClient client, BlockingQueue<PageUrl> urlQueue, Set<URL> visitedUrls, PageProcessor processor, UrlMatcher matcher ) {
		this.client = client;
		this.urlQueue = urlQueue;
		this.visitedUrls = visitedUrls;
		this.processor = processor;
		this.matcher = matcher;
	}

	@Override
	public void run( ) {
		try {
			while ( true ) {
				PageUrl url = this.urlQueue.take( );
				
				if ( !this.matcher.allow( url ) )
					continue;
				
				if ( !this.visitedUrls.add( url.getUrl( ) ) )
					continue;
				
				HttpGet get = null;
				try {
					get = new HttpGet( new URI( url.getUrl( ).getProtocol( ), url.getUrl( ).getHost( ), url.getUrl( ).getPath( ) , url.getUrl( ).getQuery( ), null ) );
					HttpResponse response = this.client.execute( get );
					this.urlQueue.addAll( this.processor.process( url, response.getStatusLine( ).getStatusCode( ), EntityUtils.toByteArray( response.getEntity( ) ) ) );
					
				} catch( Exception e ) {
					e.printStackTrace( );
				} finally {
					if ( get != null )
						get.releaseConnection( );
				}
			}
		} catch( InterruptedException e ) {
			return;
		}
	}

}
