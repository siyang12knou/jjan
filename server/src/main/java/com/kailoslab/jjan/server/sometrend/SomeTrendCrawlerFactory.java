package com.kailoslab.jjan.server.sometrend;

import com.kailoslab.jjan.server.data.AssociationRepository;
import com.kailoslab.jjan.server.data.CodeRepository;
import com.kailoslab.jjan.server.data.MentionRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Getter
@Component
public class SomeTrendCrawlerFactory extends BasePooledObjectFactory<SomeTrendCrawler> {
    @Getter
    private final CodeRepository codeRepository;
    @Getter
    private final MentionRepository mentionRepository;
    @Getter
    private final AssociationRepository associationRepository;
    @Getter
    @Value("${kailoslab.download-dir}")
    private String downloadDir;
    @Getter
    @Setter
    private WebDriver firstTabDriver;

    @Override
    public SomeTrendCrawler create() throws Exception {
        return new SomeTrendCrawler(this);
    }

    @Override
    public PooledObject wrap(SomeTrendCrawler someTrendCrawler) {
        return new DefaultPooledObject<>(someTrendCrawler);
    }
}
