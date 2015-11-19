# ADS-NewsArticles


The generation of the dataset should be carried out through the NewsArticleScraper class
where the input file a .csv file containing a URL per line together with the publishing 
date separated by a space. The local output file path should be given as an argument which 
is a .jsonld file. 

The setting for the news publisher can be set though the NewsSourceDataModel by extending 
or initializing it. Two example have been given in domain.news.sources. The contain the 
exact XPaths of the metadata to be extracted. 

JSONProperties are the attributes for the output JSON-LD file. The output file will only
include those articles for which at least a headline and an article body can be extracted.

The content analysis is done with TextRazor for which an API key is needed. Insert your API
key in TextAnalyzer. The TextRazor Java SDK is needed for that.
