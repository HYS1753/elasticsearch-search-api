# Elasticsearch search API

--- 

## Overview
이 프로젝트는 Elasticsearch를 기반으로 한 검색 API의 예제입니다. 
Spring Boot를 활용하여 효율적이고 확장 가능한 애플리케이션을 개발하였으며, 
기본적인 Elasticsearch 검색 쿼리를 제공합니다.
- aggregation
- query
  - bool(must, should, must not, filter)
  - multi_match
  - function_score
  - match
- paging
- sort
- etc...

또한, Apache Kafka와의 통합을 통해 사용자 쿼리가 비동기적으로 스트리밍되고 저장되도록 구성되었습니다. 
이를 통해 검색 패턴과 사용자 상호작용을 추적, 분석 및 처리할 수 있는 기반을 제공합니다.

## Tech Stack
- JAVA 17
- Gradle 8.10
- Springboot 3.3.2
- springdoc-openapi-starter-webmvc-ui 2.2.0
- spring-boot-starter-data-elasticsearch 3.1.0
- spring-kafka 3.2.2

## Prerequisites
다음과 같은 서비스가 로컬 또는 외부에서 실행되고 있어야 합니다.

(default 설정은 localhost 이며, 외부 서비스를 사용하기 위해서는 application.yml을 확인해 주세요.)
- Kafka
- Elasticsearch

## Usage
### Basic Search Query
- Endpoint: `/api/v1/search`
- Params: `query, author, publisher, page, pagesize`
- Description: 도서명, 출판사, 저자 기준으로 상품을 검색하는 예시 API 입니다.

**Request Example:**
```json
http://localhost:8080/api/v1/search?query=python
```
**Response Example:**
```json
{
  "query": "python",
  "totalSize": 97,
  "realSize": 5,
  "aggregations": {
    "range": [
      {
        "name": "PRICE_AGG",
        "buckets": [
          {
            "key": "*-10000.0",
            "from": 0,
            "to": 10000,
            "docCount": 1
          },
          {
            "key": "10000.0-20000.0",
            "from": 10000,
            "to": 20000,
            "docCount": 17
          },
          {
            "key": "20000.0-30000.0",
            "from": 20000,
            "to": 30000,
            "docCount": 54
          },
          {
            "key": "30000.0-40000.0",
            "from": 30000,
            "to": 40000,
            "docCount": 21
          },
          {
            "key": "40000.0-50000.0",
            "from": 40000,
            "to": 50000,
            "docCount": 2
          },
          {
            "key": "50000.0-*",
            "from": 50000,
            "to": 0,
            "docCount": 2
          }
        ]
      }
    ]
  },
  "source": [
    {
      "id": "9788979148688",
      "bookName": "Python",
      "publisher": "한빛미디어",
      "author": "폴 배리|강권학",
      "barcode": "9788979148688",
      "price": "30000.0",
      "rlseDate": "2011-10-28"
    },
    {
      "id": "9788998955953",
      "bookName": "파이썬(Python)",
      "publisher": "아티오",
      "author": "박병기",
      "barcode": "9788998955953",
      "price": "23000.0",
      "rlseDate": "2017-01-30"
    },
    {
      "id": "9788965872023",
      "bookName": "Python 프로그래밍",
      "publisher": "상학당",
      "author": "남상엽|김상범|강현웅",
      "barcode": "9788965872023",
      "price": "30000.0",
      "rlseDate": "2020-09-15"
    },
    {
      "id": "9788992649681",
      "bookName": "Python Cookbook",
      "publisher": "인피니티북스",
      "author": "데이비드 비즐리|브라이언 K. 존스|정승원",
      "barcode": "9788992649681",
      "price": "38000.0",
      "rlseDate": "2014-02-05"
    },
    {
      "id": "9788960776982",
      "bookName": "Black Hat Python",
      "publisher": "에이콘출판",
      "author": "저스틴 지이츠|민병호",
      "barcode": "9788960776982",
      "price": "25000.0",
      "rlseDate": "2015-04-30"
    }
  ]
}
```
**Excuted Elasticsearch Query Example:**
```json
{
    "track_total_hits": true,
    "aggs": {
        "PRICE_AGG": {
            "range": {
                "field": "PRICE",
                "ranges": [
                    {
                        "to": "10000"
                    },
                    {
                        "from": "10000",
                        "to": "20000"
                    },
                    {
                        "from": "20000",
                        "to": "30000"
                    },
                    {
                        "from": "30000",
                        "to": "40000"
                    },
                    {
                        "from": "40000",
                        "to": "50000"
                    },
                    {
                        "from": "50000"
                    }
                ]
            }
        }
    },
    "query": {
        "function_score": {
            "boost_mode": "replace",
            "functions": [
                {
                    "script_score": {
                        "script": {
                            "source": "_score"
                        }
                    }
                }
            ],
            "query": {
                "bool": {
                    "filter": [],
                    "minimum_should_match": "0",
                    "must": [
                        {
                            "multi_match": {
                                "boost": 1,
                                "fields": [
                                    "BOOK_NAME",
                                    "AUTHOR",
                                    "PUBLISHER",
                                    "BARCODE"
                                ],
                                "operator": "and",
                                "query": "python",
                                "type": "cross_fields"
                            }
                        }
                    ],
                    "must_not": [],
                    "should": [
                        {
                            "match_phrase": {
                                "BOOK_NAME": {
                                    "boost": 1,
                                    "query": "python",
                                    "slop": 3
                                }
                            }
                        }
                    ]
                }
            },
            "score_mode": "sum"
        }
    },
    "from": 0,
    "size": 5
}
```

## UI Preview
### Swagger
Entry Point: http://localhost:8080/swagger-ui/index.html
![swagger-ui](https://github.com/user-attachments/assets/d85e6d53-8035-44d8-8046-c380fd4d6d58)

### Elasticsearch Index Setting
book-test
![index setting](https://github.com/user-attachments/assets/34edbcf4-f36c-48eb-8e45-61c56ecc1c01)

### Kafka
User log example
![kafka-ui](https://github.com/user-attachments/assets/380578a4-bb2c-4254-8de0-f9ddc124875c)