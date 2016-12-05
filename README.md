# SNP Filter

Currently, employed to detect the posts containing terrorist propaganda. SNP filter, reads the social media posts and filters the results based on three specifications:
  * Terrorism Tags
  * Motivation Tags
  * Out-of-context (OOC) Tags
  
Sample content of each Tag set is given in the table below:

Tag Set  | Content
------------- | -------------
Terrorist Tags (S1)  | {Terrorist, Terrorism, etc.}
Motivation Tags (S2)  | {Encourage, Inspire, etc.}
OOC Tags (S3)  | {"Name of People from Unrelated Context", etc.}


### Filter Algorithm

```{r, tidy=FALSE, eval=FALSE, highlight=FALSE }
Input : a new twitter
Out put : Accept or not
 if the twitter contains words in filter set 1 then 
    if the twitter contains words in filter set 2 then
        if the twitter contains words in filter set 3 then
               return not accepted
        else
               return accepted 

```

### Limitations

#### 1) Multiple Nouns

Suiside Belt

Since our matching algorithm workds via tokenized key word matching, such that it can't work well on Multiple Nouns
For example, it will be hard to filter out a twitter that contains the object _Suicide Belt_.
