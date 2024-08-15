package elasticsearch.config.swagger;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.tags.Tag;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * Swagger 사용하기 위한 Configuration
 */
@OpenAPIDefinition(
        info = @Info(title = "Elasticsearch 검색 API",
                description = "Elasticsearch 검색 API 명세서 (<A href = 'https://github.com/HYS1753/elasticsearch-search-api'>github URL</A>)",
                version = "v1"))
@Configuration
public class SwaggerConfig {

    private List<Server> getServersItem() {
        List<Server> servers = new ArrayList<Server>();
        servers.add(new Server().url("/"));
        return servers;
    }

    /**
     * Swagger 설정들이 담긴 Docket Bean 등록
     */
    @Bean
    public GroupedOpenApi AllApi() {
        List<Tag> allTags = new ArrayList<>();
        return GroupedOpenApi.builder()
                .group("all")
                .pathsToMatch("/api/**")
                .addOpenApiCustomizer(openApi -> {
                    openApi.setTags(allTags);
                    openApi.servers(getServersItem());
                })
                .build();
    }
}
