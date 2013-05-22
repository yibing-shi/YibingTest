package com.yibing;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class RegularExpressionTest {

    @DataProvider(name = "dataProvider")
    public Object[][] provider() {
        return new Object[][] {
                { "http://any.com", "http://any.com" },
                { "https://any.com", "https://any.com" },
                { "http://any.com/", "http://any.com" },
                { "https://any.com/", "https://any.com" },
                { "http://any.com/any/page", "http://any.com/any/page" },
                { "https://any.com/any/page", "https://any.com/any/page" },
                { "http://any.com/any/page/", "http://any.com/any/page" },
                { "https://any.com/any/page/", "https://any.com/any/page" },
                { "http://www.any.com/any/page", "http://www.any.com/any/page" },
                { "https://www.any.com/any/page", "https://www.any.com/any/page" },
                { "http://any.com/any/page?p1=1&p2=1", "http://any.com/any/page" },
                { "https://any.com/any/page?p1=1&p2=1", "https://any.com/any/page" },
                { "http://any.site.com/any/page?p1=1&p2=1", "http://any.site.com/any/page" },
                { "https://any.site.com/any/page?p1=1&p2=1", "https://any.site.com/any/page" },
                { "http://any.com/any/page#", "http://any.com/any/page" },
                { "https://any.com/any/page#", "https://any.com/any/page" },
                { "http://any-site.com/any-page/any-sub-page", "http://any-site.com/any-page/any-sub-page" },
                { "https://any-site.com/any-page/any-sub-page", "https://any-site.com/any-page/any-sub-page" },
                { "http://m.news24.com/news24/World/News/Muslim-world-wants-sharia-as-law-of-land-20130430", "http://m.news24.com/news24/World/News/Muslim-world-wants-sharia-as-law-of-land-20130430" }
        };
    }

    @Test(dataProvider = "dataProvider")
    public void test(final String pageUrl, final String expectedResult) {
        final String regex = "^((https?|ftp)://)?((.*?)(:(.*?)|)@)?([^:/\\s]+)(:[^/]*)?((/\\w+)*/)?([-\\w\\.]+[^#\\?\\s]*)?(\\?([^#]*))?(#(.*))?";
        //                    ------------------ ---------------   ----------  ------  ----------- ----------------------- ------------  -----
        //                    PROTOCOL            USER:PASSWORD      HOST       PORT     PATH            FILE               QUERY_PARA   FRAGMENT
        final String result = pageUrl.replaceAll(regex, "$1$7$8$9$11").replaceAll("/$", "");
        Assert.assertTrue(pageUrl.matches(regex));
        Assert.assertEquals(result, expectedResult);
    }
}
