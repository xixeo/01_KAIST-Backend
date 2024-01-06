package kr.co.igns.framework.utils.excel;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public abstract class ExcelConfig implements WebMvcConfigurer {

    private ExcelXlsView excelXlsView;
    private ExcelXlsxView excelXlsxView;
    private ExcelXlsxStreamingView excelXlsxStreamingView;

    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        registry.enableContentNegotiation(excelXlsView, excelXlsxView, excelXlsxStreamingView);
    }
}
