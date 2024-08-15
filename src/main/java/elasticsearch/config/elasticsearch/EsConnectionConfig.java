package elasticsearch.config.elasticsearch;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import org.elasticsearch.client.RestClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchClients;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;

@Configuration
public class EsConnectionConfig {

    private EsConfig esConfig;

    public EsConnectionConfig(EsConfig esConfig) {
        this.esConfig = esConfig;
    }

    @Bean(name="esOperations")
    public ElasticsearchOperations esOperations() {
        RestClient restClient = ElasticsearchClients.getRestClient(esConfig.clientConfiguration());
        ElasticsearchClient elasticsearchClient = ElasticsearchClients.createImperative(restClient);
        return new ElasticsearchTemplate(elasticsearchClient);
    }

    @Bean(name="esClient")
    public ElasticsearchClient esClient() {
        RestClient restClient = ElasticsearchClients.getRestClient(esConfig.clientConfiguration());
        return ElasticsearchClients.createImperative(restClient);
    }
}
