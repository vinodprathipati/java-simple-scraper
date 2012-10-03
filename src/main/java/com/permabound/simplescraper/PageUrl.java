package com.permabound.simplescraper;

import java.net.URL;

public class PageUrl {

	private URL url;
	
	private URL referringUrl;
	
	public PageUrl( URL url, URL referringUrl ) {
		this.url = url;
		this.referringUrl = referringUrl;
	}

	public URL getUrl( ) {
		return this.url;
	}

	public URL getReferringUrl( ) {
		return this.referringUrl;
	}
	
	@Override
	public boolean equals( Object o ) {
		if ( !( o instanceof PageUrl ) )
			return false;
		
		PageUrl oo = ( PageUrl ) o;
		return ( this.getUrl( ) != null && oo.getUrl( ) != null && oo.getUrl( ).equals( this.getUrl( ) ) ) &&
			( this.getReferringUrl( ) != null && oo.getReferringUrl( ) != null && oo.getReferringUrl( ).equals( this.getReferringUrl( ) ) );
	}
	
	@Override
	public int hashCode( ) {
		return ( this.getUrl( ) != null ? Math.abs( this.getUrl( ).hashCode( ) ) : 0 ) - ( this.getReferringUrl( ) != null ? Math.abs( this.getReferringUrl( ).hashCode( ) ) : 0 );
	}

}
