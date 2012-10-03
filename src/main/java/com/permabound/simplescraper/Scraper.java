package com.permabound.simplescraper;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;

public class Scraper {

	private int threadCount;
	
	private HttpClient client;
	
	private BlockingQueue<PageUrl> urlQueue = new LinkedBlockingDeque<PageUrl>( );
	
	private Set<URL> visitedUrls = Collections.synchronizedSet( new HashSet<URL>( ) );
	
	private List<Thread> threads;
	
	public static void main( String[ ] args ) throws Exception {
		Scraper s = new Scraper( 5 );
		s.run( new URL( "http://www.perma-bound.com/" ) );
	}
	
	public Scraper( int threadCount ) {
		this.threadCount = threadCount;

		ClientConnectionManager cm = new PoolingClientConnectionManager( );
		this.client = new DefaultHttpClient( cm );
		
		this.threads = new ArrayList<Thread>( this.threadCount );

	}

	public void run( URL... urls ) {
		for ( URL url : urls )
			this.urlQueue.add( new PageUrl( url, null ) );
		
		List<Thread> threads = new ArrayList<Thread>( this.threadCount );
		for ( int i = 0; i < this.threadCount; i++ ) {
			ScraperThread thread = new ScraperThread( this.client, this.urlQueue, this.visitedUrls, new SimpleJSoupPageProcessor( ), new UrlMatcher( ) {
				@Override
				public boolean allow( PageUrl url ) {
					return url.getUrl( ).toString( ).contains( "perma-bound.com" ) ;
				}
				
			} );
			thread.run( );
			threads.add( thread );
		}
		
		for ( Thread t : threads ) {
			try {
				t.join( );
			} catch ( InterruptedException e ) {
			}
		}
	}

}
