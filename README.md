# ADS-NewsArticles


The generation of the dataset should be carried out through the NewsArticleScraper class
which takes as input a .csv file containing a URL per line together with the publishing 
date separated by a space. The files used can be located in the /data directory. A local 
output file path should be given as an argument in the .jsonld format. 

An example of how NewsArticleScraper can be be called is given in the class 
DatasetGenerationExample.java, whereas the FileManager usage is demonstrated in 
ArticleDataManagementExample.java.

The settings for the news publisher can be set though the NewsSourceDataModel by extending 
or initializing it. Two examples have been given in domain.news.sources. They contain the 
exact XPaths of the metadata to be extracted. Further news domain is recommended to be 
defined there, but can be also instantiated as needed. 

The attributes for the JSON-LD file are defined in JSONProperties, and would typically not 
require any changes. 

The output file will only include those articles for which at least a headline and an article 
body can be extracted. This means that any other attributes for which tags have been defined 
in NewsSourceDataModel will optionally be included if existent. 

The content analysis of the article text is done with TextRazor for which an API key is needed. 
Insert your API key in TextAnalyzer. The TextRazor Java SDK is needed for that, which can be 
downloaded from www.textrazor.com. 
