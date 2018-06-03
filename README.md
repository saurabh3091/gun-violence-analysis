# US Gun Violence Analyses

### Problem Statement
For a given dataset on Kaggle
https://www.kaggle.com/jameslko/gun-violence-data

* To figure out:
    1. Which States are most violent
    2. Yearly trend of gun related violence
    3. Total injuries and killings till now
    4. Types of guns used
    5. Gender and Age wise participants in gun related crimes
### Prerequisites

Gradle, 
Scala 2.11,
Spark 2.2.1

### Build
goto the gun-violence-analysis directory and run
```
gradle build
```

## Run on Local

```
spark-submit --master "local[*]" --class com.data.GunViolenceAnalyzer build/libs/gun-violence-analysis-0.1.jar path-to-csv
```


## Run on YARN

```
export HADOOP_CONF_DIR=XXX
spark-submit \
  --class com.data.GunViolenceAnalyzer \
  --master yarn \
  --deploy-mode cluster \  # can be client for client mode
  --executor-memory 128M \
  --num-executors 3 \
  build/libs/gun-violence-analysis-0.1.jar path-to-csv
```

## Output
Will be stored as csv file in locations specified in config.yaml