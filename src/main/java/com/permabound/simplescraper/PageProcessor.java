package com.permabound.simplescraper;

import java.util.Collection;

public interface PageProcessor {

	public Collection<PageUrl> process( PageUrl url, int status, byte[ ] payload );
	
}
