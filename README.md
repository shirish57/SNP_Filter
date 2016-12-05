# SNP Filter

Currently, employed to detect the posts containing terrorist propaganda. SNP filter, reads the social media posts and filters the results based on three specifications:
  * Terrorism Tags 
  * Motivation Tags
  * Out-of-context (OOC) Tags
  
Sample content of each Tag set is given in the table below:

Tag Set  | Content
------------- | -------------
Terrorist Tags (S1)  | Specifically terrorism agents or terrorism actions,  such as {isis, kill, threaten,taliban}
Motivation Tags (S2)  | {Encourage, Inspire, etc.}
OOC Tags (S3)  | {"Name of People from Unrelated Context", etc.}


### Preprocess Data
```{r, tidy=FALSE, eval=FALSE, highlight=FALSE }
Input: twitter
Output: preprocessedTwitter
1) Split the post into independent sentences 
2) Remove the stop words from text
3) Decompose compound data, such as #supportISIS=>support ISIS
3) Stem the words to root words, such as support, supported, supporting=> support
```

### Filter Algorithm

```{r, tidy=FALSE, eval=FALSE, highlight=FALSE }
Input : a new twitter
Out put : Accept or not
1)if the twitter contains words in filter set 1 then 
2)   if the twitter contains words in filter set 2 then
3)        if the twitter contains words in filter set 3 then
4)               return not accepted
5)        else
6)               return accepted 

```





### Limitations

#### 1) Multiple Nouns

Example of multiple nouns : Suiside Belt, cut their heads off, call upon 

Since our matching algorithm workds via  key word matching in a 1-gram model, such that it can't work well on Multiple Nouns.
For example, it will be hard to filter out a twitter that contains the object _Suicide Belt_. The problem happens because when we break down the sencenten, the smallest unit after dividing is a token made up of one word, such as embasy, isis, division etc. In the meantime, the tags sets are also made up of single word, such as terrorism, isis, threaten.
