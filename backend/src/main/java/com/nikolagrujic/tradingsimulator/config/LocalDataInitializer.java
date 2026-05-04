package com.nikolagrujic.tradingsimulator.config;

import com.nikolagrujic.tradingsimulator.model.Portfolio;
import com.nikolagrujic.tradingsimulator.model.PortfolioHistory;
import com.nikolagrujic.tradingsimulator.model.NewsArticle;
import com.nikolagrujic.tradingsimulator.model.StockInfo;
import com.nikolagrujic.tradingsimulator.model.User;
import com.nikolagrujic.tradingsimulator.repository.NewsRepository;
import com.nikolagrujic.tradingsimulator.repository.PortfolioHistoryRepository;
import com.nikolagrujic.tradingsimulator.repository.PortfolioRepository;
import com.nikolagrujic.tradingsimulator.repository.StockInfoRepository;
import com.nikolagrujic.tradingsimulator.repository.UserRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class LocalDataInitializer implements CommandLineRunner {
    private final StockInfoRepository stockInfoRepository;
    private final UserRepository userRepository;
    private final PortfolioRepository portfolioRepository;
    private final PortfolioHistoryRepository portfolioHistoryRepository;
    private final NewsRepository newsRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public LocalDataInitializer(
        StockInfoRepository stockInfoRepository,
        UserRepository userRepository,
        PortfolioRepository portfolioRepository,
        PortfolioHistoryRepository portfolioHistoryRepository,
        NewsRepository newsRepository,
        BCryptPasswordEncoder passwordEncoder
    ) {
        this.stockInfoRepository = stockInfoRepository;
        this.userRepository = userRepository;
        this.portfolioRepository = portfolioRepository;
        this.portfolioHistoryRepository = portfolioHistoryRepository;
        this.newsRepository = newsRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        seedStocks();
        seedNews();
        seedDemoUser();
    }

    private void seedStocks() {
        List<StockInfo> stocks = Arrays.asList(
            stock("AAPL", "Apple Inc.", "NASDAQ", "XNAS", "United States", "USD", "182.52"),
            stock("MSFT", "Microsoft Corporation", "NASDAQ", "XNAS", "United States", "USD", "421.90"),
            stock("GOOGL", "Alphabet Inc.", "NASDAQ", "XNAS", "United States", "USD", "164.37"),
            stock("AMZN", "Amazon.com Inc.", "NASDAQ", "XNAS", "United States", "USD", "185.01"),
            stock("TSLA", "Tesla Inc.", "NASDAQ", "XNAS", "United States", "USD", "172.63"),
            stock("JPM", "JPMorgan Chase & Co.", "NYSE", "XNYS", "United States", "USD", "198.48")
        );

        for (StockInfo stock : stocks) {
            if (!stockInfoRepository.existsBySymbol(stock.getSymbol())) {
                stockInfoRepository.save(stock);
            }
        }
    }

    private StockInfo stock(String symbol, String name, String exchange, String micCode, String country, String currency, String price) {
        StockInfo stock = new StockInfo();
        stock.setSymbol(symbol);
        stock.setName(name);
        stock.setExchange(exchange);
        stock.setMicCode(micCode);
        stock.setCountry(country);
        stock.setCurrency(currency);
        stock.setCurrentPrice(new BigDecimal(price));
        stock.setLastUpdated(LocalDateTime.now());
        return stock;
    }

    private void seedDemoUser() {
        if (userRepository.findByEmail("demo@example.com") != null) {
            return;
        }

        User user = new User();
        user.setFirstName("Demo");
        user.setLastName("Trader");
        user.setEmail("demo@example.com");
        user.setPassword(passwordEncoder.encode("Password123"));
        user.setEmailVerified(true);
        user.setVerificationDate(LocalDate.now());
        userRepository.save(user);

        Portfolio portfolio = new Portfolio();
        portfolio.setUser(user);
        portfolioRepository.save(portfolio);

        PortfolioHistory history = new PortfolioHistory();
        history.setPortfolio(portfolio);
        history.setDate(LocalDate.now());
        history.setPortfolioRank(1L);
        history.setTotalValue(new BigDecimal("30000.00"));
        portfolioHistoryRepository.save(history);
    }

    private void seedNews() {
        List<NewsArticle> articles = Arrays.asList(
            article(
                "Market Desk",
                "Technology shares steady as traders digest earnings",
                "Large-cap technology stocks held firm while investors reviewed recent earnings and interest-rate expectations.",
                "https://example.com/markets/technology-shares-steady",
                "https://images.unsplash.com/photo-1640340434855-6084b1f4901c?auto=format&fit=crop&w=1200&q=80"
            ),
            article(
                "Trading Simulator",
                "Banking and retail names lead a mixed session",
                "Financial and consumer stocks moved in different directions as volume stayed moderate across major exchanges.",
                "https://example.com/markets/mixed-session",
                "https://images.unsplash.com/photo-1650959828226-f9d53a7c1f64?auto=format&fit=crop&w=1200&q=80"
            ),
            article(
                "Analyst Wire",
                "Portfolio discipline remains key in volatile markets",
                "Analysts pointed to position sizing and cash management as important habits for new market participants.",
                "https://example.com/markets/portfolio-discipline",
                "https://images.unsplash.com/photo-1611974789855-9c2a0a7236a3?auto=format&fit=crop&w=1200&q=80"
            )
        );

        for (NewsArticle article : articles) {
            if (!newsRepository.existsByTitle(article.getTitle())) {
                newsRepository.save(article);
            }
        }
    }

    private NewsArticle article(String author, String title, String description, String url, String urlToImage) {
        NewsArticle article = new NewsArticle();
        article.setAuthor(author);
        article.setTitle(title);
        article.setDescription(description);
        article.setUrl(url);
        article.setUrlToImage(urlToImage);
        article.setPublishedAt(LocalDateTime.now());
        return article;
    }
}
