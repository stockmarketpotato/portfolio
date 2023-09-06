package name.abuchen.portfolio.datatransfer.pdf.tastytrade;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.junit.Assert.assertNull;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;

import name.abuchen.portfolio.datatransfer.Extractor;
import name.abuchen.portfolio.datatransfer.Extractor.BuySellEntryItem;
import name.abuchen.portfolio.datatransfer.Extractor.Item;
import name.abuchen.portfolio.datatransfer.Extractor.SecurityItem;
import name.abuchen.portfolio.datatransfer.Extractor.TransactionItem;
import name.abuchen.portfolio.datatransfer.actions.AssertImportActions;
import name.abuchen.portfolio.datatransfer.pdf.PDFInputFile;
import name.abuchen.portfolio.datatransfer.pdf.TastytradePDFExtractor;
import name.abuchen.portfolio.model.AccountTransaction;
import name.abuchen.portfolio.model.BuySellEntry;
import name.abuchen.portfolio.model.Client;
import name.abuchen.portfolio.model.PortfolioTransaction;
import name.abuchen.portfolio.model.Transaction.Unit;
import name.abuchen.portfolio.money.CurrencyUnit;
import name.abuchen.portfolio.money.Money;
import name.abuchen.portfolio.money.Values;

/**
 * From
 * https://forum.portfolio-performance.info/t/pdf-import-from-tastytrade/24238/5
 */
@SuppressWarnings("nls")
public class TastytradePDFExtractorTest
{
    @Test
    public void testSummaryStatement01()
    {
        TastytradePDFExtractor extractor = new TastytradePDFExtractor(new Client());
        List<Exception> errors = new ArrayList<>();
        List<Item> results = extractor.extract(PDFInputFile.loadTestCase(getClass(), "SummaryStatement01.txt"), errors);

        assertThat(errors, empty());
        assertThat(results.size(), is(2));
        new AssertImportActions().check(results, CurrencyUnit.USD);

        // check transaction
        Iterator<Extractor.Item> iter = results.stream().filter(BuySellEntryItem.class::isInstance).iterator();
        assertThat(results.stream().filter(BuySellEntryItem.class::isInstance).count(), is(1L));
        assertThat(results.stream().filter(SecurityItem.class::isInstance).count(), is(1L));

        Item item = iter.next();

        // assert transaction
        PortfolioTransaction P = ((BuySellEntry) item.getSubject()).getPortfolioTransaction();
        assertThat(P.getType(), is(PortfolioTransaction.Type.BUY));
        assertThat(P.getMonetaryAmount(), is(Money.of(CurrencyUnit.USD, Values.Amount.factorize(2324.24))));
        assertThat(P.getCurrencyCode(), is(CurrencyUnit.USD));
        assertThat(P.getDateTime(), is(LocalDateTime.parse("2023-07-17T00:00")));
        assertThat(P.getUnitSum(Unit.Type.FEE), is(Money.of(CurrencyUnit.USD, Values.Amount.factorize(0.0))));
        assertNull(P.getNote());
        assertThat(P.getSecurity().getWkn(), is("922908736")); // CUSIP
        assertNull(P.getSecurity().getIsin());
        assertThat(P.getSecurity().getTickerSymbol(), is("VUG"));
        assertThat(P.getSecurity().getName(), is("VANGUARD INDEX FUNDS VANGUARD GROWTH ETF"));
        assertThat(P.getSecurity().getCurrencyCode(), is(CurrencyUnit.USD));
        assertThat(P.getShares(), is(Values.Share.factorize(8.)));

        assertThat(P.getUnitSum(Unit.Type.TAX), is(Money.of(CurrencyUnit.USD, Values.Amount.factorize(0.00))));
        assertThat(P.getGrossPricePerShare().toMoney(),
                        is(Money.of(CurrencyUnit.USD, Values.Amount.factorize(290.5298))));
    }

    @Test
    public void testSummaryStatement02()
    {
        TastytradePDFExtractor extractor = new TastytradePDFExtractor(new Client());
        List<Exception> errors = new ArrayList<>();
        List<Item> results = extractor.extract(PDFInputFile.loadTestCase(getClass(), "SummaryStatement02.txt"), errors);

        assertThat(errors, empty());
        assertThat(results.size(), is(2));
        new AssertImportActions().check(results, CurrencyUnit.USD);

        // check transaction
        Iterator<Extractor.Item> iter = results.stream().filter(BuySellEntryItem.class::isInstance).iterator();
        assertThat(results.stream().filter(BuySellEntryItem.class::isInstance).count(), is(1L));
        assertThat(results.stream().filter(SecurityItem.class::isInstance).count(), is(1L));

        Item item = iter.next();

        // assert transaction
        PortfolioTransaction P = ((BuySellEntry) item.getSubject()).getPortfolioTransaction();
        assertThat(P.getCurrencyCode(), is(CurrencyUnit.USD));
        assertThat(P.getSecurity().getCurrencyCode(), is(CurrencyUnit.USD));
        assertThat(P.getType(), is(PortfolioTransaction.Type.SELL));
        assertThat(P.getMonetaryAmount(), is(Money.of(CurrencyUnit.USD, Values.Amount.factorize(8450.91))));
        assertThat(P.getDateTime(), is(LocalDateTime.parse("2023-06-22T00:00")));
        assertThat(P.getUnitSum(Unit.Type.FEE), is(Money.of(CurrencyUnit.USD, Values.Amount.factorize(0.09))));
        assertNull(P.getNote());
        assertThat(P.getSecurity().getWkn(), is("67066G104")); // CUSIP
        assertNull(P.getSecurity().getIsin());
        assertThat(P.getSecurity().getTickerSymbol(), is("NVDA"));
        assertThat(P.getSecurity().getName(), is("NVIDIA CORP"));
        assertThat(P.getShares(), is(Values.Share.factorize(20.)));
        assertThat(P.getUnitSum(Unit.Type.TAX), is(Money.of(CurrencyUnit.USD, Values.Amount.factorize(0.00))));
        assertThat(P.getGrossPricePerShare().toMoney(),
                        is(Money.of(CurrencyUnit.USD, Values.Amount.factorize(422.55))));
    }

    @Test
    public void testSummaryStatement03()
    {
        TastytradePDFExtractor extractor = new TastytradePDFExtractor(new Client());
        List<Exception> errors = new ArrayList<>();
        List<Item> results = extractor.extract(PDFInputFile.loadTestCase(getClass(), "SummaryStatement03.txt"), errors);

        assertThat(errors, empty());
        assertThat(results.size(), is(6));
        new AssertImportActions().check(results, CurrencyUnit.USD);

        // check transaction
        Iterator<Extractor.Item> iter = results.stream().filter(BuySellEntryItem.class::isInstance).iterator();
        assertThat(results.stream().filter(BuySellEntryItem.class::isInstance).count(), is(3L));
        assertThat(results.stream().filter(SecurityItem.class::isInstance).count(), is(3L));

        Item item = iter.next();

        PortfolioTransaction P = ((BuySellEntry) item.getSubject()).getPortfolioTransaction();
        assertThat(P.getCurrencyCode(), is(CurrencyUnit.USD));
        assertThat(P.getSecurity().getCurrencyCode(), is(CurrencyUnit.USD));
        assertThat(P.getType(), is(PortfolioTransaction.Type.SELL));
        assertThat(P.getMonetaryAmount(), is(Money.of(CurrencyUnit.USD, Values.Amount.factorize(2089.06))));
        assertThat(P.getDateTime(), is(LocalDateTime.parse("2022-12-30T00:00")));
        assertThat(P.getUnitSum(Unit.Type.FEE), is(Money.of(CurrencyUnit.USD, Values.Amount.factorize(0.09))));
        assertNull(P.getNote());
        assertThat(P.getSecurity().getWkn(), is("18915M107")); // CUSIP
        assertNull(P.getSecurity().getIsin());
        assertThat(P.getSecurity().getTickerSymbol(), is("NET"));
        assertThat(P.getSecurity().getName(), is("CLOUDFLARE INC CLASS A COMMON STOCK PAR VALUE $0.001 PER SHARE"));
        assertThat(P.getShares(), is(Values.Share.factorize(47.)));
        assertThat(P.getUnitSum(Unit.Type.TAX), is(Money.of(CurrencyUnit.USD, Values.Amount.factorize(0.00))));
        assertThat(P.getGrossPricePerShare().toMoney(),
                        is(Money.of(CurrencyUnit.USD, Values.Amount.factorize(44.4501))));

        item = iter.next();
        P = ((BuySellEntry) item.getSubject()).getPortfolioTransaction();
        assertThat(P.getCurrencyCode(), is(CurrencyUnit.USD));
        assertThat(P.getSecurity().getCurrencyCode(), is(CurrencyUnit.USD));
        assertThat(P.getType(), is(PortfolioTransaction.Type.SELL));
        assertThat(P.getMonetaryAmount(), is(Money.of(CurrencyUnit.USD, Values.Amount.factorize(719.52))));
        assertThat(P.getDateTime(), is(LocalDateTime.parse("2022-12-30T00:00")));
        assertThat(P.getUnitSum(Unit.Type.FEE), is(Money.of(CurrencyUnit.USD, Values.Amount.factorize(0.03))));
        assertNull(P.getNote());
        assertThat(P.getSecurity().getWkn(), is("67066G104")); // CUSIP
        assertNull(P.getSecurity().getIsin());
        assertThat(P.getSecurity().getTickerSymbol(), is("NVDA"));
        assertThat(P.getSecurity().getName(), is("NVIDIA CORP"));
        assertThat(P.getShares(), is(Values.Share.factorize(5.)));
        assertThat(P.getUnitSum(Unit.Type.TAX), is(Money.of(CurrencyUnit.USD, Values.Amount.factorize(0.00))));
        assertThat(P.getGrossPricePerShare().toMoney(),
                        is(Money.of(CurrencyUnit.USD, Values.Amount.factorize(143.9103))));

        item = iter.next();
        P = ((BuySellEntry) item.getSubject()).getPortfolioTransaction();
        assertThat(P.getCurrencyCode(), is(CurrencyUnit.USD));
        assertThat(P.getSecurity().getCurrencyCode(), is(CurrencyUnit.USD));
        assertThat(P.getType(), is(PortfolioTransaction.Type.SELL));
        assertThat(P.getMonetaryAmount(), is(Money.of(CurrencyUnit.USD, Values.Amount.factorize(2666.78))));
        assertThat(P.getDateTime(), is(LocalDateTime.parse("2022-12-30T00:00")));
        assertThat(P.getUnitSum(Unit.Type.FEE), is(Money.of(CurrencyUnit.USD, Values.Amount.factorize(0.09))));
        assertNull(P.getNote());
        assertThat(P.getSecurity().getWkn(), is("88160R101")); // CUSIP
        assertNull(P.getSecurity().getIsin());
        assertThat(P.getSecurity().getTickerSymbol(), is("TSLA"));
        assertThat(P.getSecurity().getName(), is("TESLA INC COMMON STOCK"));
        assertThat(P.getShares(), is(Values.Share.factorize(22.)));
        assertThat(P.getUnitSum(Unit.Type.TAX), is(Money.of(CurrencyUnit.USD, Values.Amount.factorize(0.00))));
        assertThat(P.getGrossPricePerShare().toMoney(),
                        is(Money.of(CurrencyUnit.USD, Values.Amount.factorize(121.2212))));
    }

    @Test
    public void testSummaryStatement04()
    {
        TastytradePDFExtractor extractor = new TastytradePDFExtractor(new Client());
        List<Exception> errors = new ArrayList<>();
        List<Item> results = extractor.extract(PDFInputFile.loadTestCase(getClass(), "SummaryStatement04.txt"), errors);

        assertThat(errors, empty());
        assertThat(results.size(), is(2));
        new AssertImportActions().check(results, CurrencyUnit.USD);

        // check transaction
        Iterator<Extractor.Item> iter = results.stream().filter(BuySellEntryItem.class::isInstance).iterator();
        assertThat(results.stream().filter(BuySellEntryItem.class::isInstance).count(), is(1L));
        assertThat(results.stream().filter(SecurityItem.class::isInstance).count(), is(1L));

        Item item = iter.next();

        PortfolioTransaction P = ((BuySellEntry) item.getSubject()).getPortfolioTransaction();
        assertThat(P.getCurrencyCode(), is(CurrencyUnit.USD));
        assertThat(P.getSecurity().getCurrencyCode(), is(CurrencyUnit.USD));
        assertThat(P.getType(), is(PortfolioTransaction.Type.BUY));
        assertThat(P.getDateTime(), is(LocalDateTime.parse("2023-03-24T00:00")));
        assertThat(P.getShares(), is(Values.Share.factorize(8.)));
        assertThat(P.getSecurity().getTickerSymbol(), is("MSFT"));
        assertThat(P.getGrossPricePerShare().toMoney(),
                        is(Money.of(CurrencyUnit.USD, Values.Amount.factorize(277.26))));
        assertThat(P.getUnitSum(Unit.Type.FEE), is(Money.of(CurrencyUnit.USD, Values.Amount.factorize(0.0))));
        assertThat(P.getMonetaryAmount(), is(Money.of(CurrencyUnit.USD, Values.Amount.factorize(2218.08))));
        assertThat(P.getSecurity().getName(), is("MICROSOFT CORP"));
        assertThat(P.getUnitSum(Unit.Type.TAX), is(Money.of(CurrencyUnit.USD, Values.Amount.factorize(0.00))));
        assertThat(P.getSecurity().getWkn(), is("594918104")); // CUSIP
        assertNull(P.getNote());
        assertNull(P.getSecurity().getIsin());
    }

    @Test
    public void testDepotStatement01()
    {
        TastytradePDFExtractor extractor = new TastytradePDFExtractor(new Client());
        List<Exception> errors = new ArrayList<>();
        List<Item> results = extractor.extract(PDFInputFile.loadTestCase(getClass(), "DepotStatement01.txt"), errors);

        assertThat(errors, empty());
        assertThat(results.size(), is(5));
        new AssertImportActions().check(results, CurrencyUnit.USD);

        // check transaction
        Iterator<Extractor.Item> iter = results.stream().filter(BuySellEntryItem.class::isInstance).iterator();
        assertThat(results.stream().filter(BuySellEntryItem.class::isInstance).count(), is(2L));
        assertThat(results.stream().filter(TransactionItem.class::isInstance).count(), is(1L));

        Item item = iter.next();

        // assert transaction
        BuySellEntry T = (BuySellEntry) item.getSubject();
        PortfolioTransaction P = T.getPortfolioTransaction();
        assertThat(P.getType(), is(PortfolioTransaction.Type.BUY));
        assertThat(P.getMonetaryAmount(), is(Money.of(CurrencyUnit.USD, Values.Amount.factorize(1306.51))));
        assertThat(P.getCurrencyCode(), is(CurrencyUnit.USD));
        assertThat(P.getDateTime(), is(LocalDateTime.parse("2023-04-24T00:00")));
        assertThat(P.getUnitSum(Unit.Type.FEE), is(Money.of(CurrencyUnit.USD, Values.Amount.factorize(0.01))));
        assertThat(P.getSecurity().getWkn(), is("46436E718")); // CUSIP
        assertNull(P.getSecurity().getIsin());
        assertThat(P.getSecurity().getName(), is("ISHARES TRUST"));
        assertThat(P.getSecurity().getCurrencyCode(), is(CurrencyUnit.USD));
        assertThat(P.getShares(), is(Values.Share.factorize(13.)));

        assertThat(P.getUnitSum(Unit.Type.TAX), is(Money.of(CurrencyUnit.USD, Values.Amount.factorize(0.00))));
        assertThat(P.getGrossPricePerShare().toMoney(),
                        is(Money.of(CurrencyUnit.USD, Values.Amount.factorize(100.50))));

        item = iter.next();

        // assert transaction
        T = (BuySellEntry) item.getSubject();
        P = T.getPortfolioTransaction();
        assertThat(P.getType(), is(PortfolioTransaction.Type.BUY));
        assertThat(P.getMonetaryAmount(), is(Money.of(CurrencyUnit.USD, Values.Amount.factorize(1284.23))));
        assertThat(P.getCurrencyCode(), is(CurrencyUnit.USD));
        assertThat(P.getDateTime(), is(LocalDateTime.parse("2023-04-25T00:00")));
        assertThat(P.getUnitSum(Unit.Type.FEE), is(Money.of(CurrencyUnit.USD, Values.Amount.factorize(0.0114))));
        assertThat(P.getSecurity().getWkn(), is("78468R663")); // CUSIP
        assertNull(P.getSecurity().getIsin());
        assertThat(P.getSecurity().getName(), is("SPDR SERIES TRUST"));
        assertThat(P.getSecurity().getCurrencyCode(), is(CurrencyUnit.USD));
        assertThat(P.getShares(), is(Values.Share.factorize(14.)));

        assertThat(P.getUnitSum(Unit.Type.TAX), is(Money.of(CurrencyUnit.USD, Values.Amount.factorize(0.00))));
        assertThat(P.getGrossPricePerShare().toMoney(),
                        is(Money.of(CurrencyUnit.USD, Values.Amount.factorize(91.7299))));

        AccountTransaction A = (AccountTransaction) results.stream().filter(TransactionItem.class::isInstance)
                        .findFirst().orElseThrow(IllegalArgumentException::new).getSubject();

        assertThat(A.getType(), is(AccountTransaction.Type.INTEREST));
        assertThat(A.getMonetaryAmount(), is(Money.of(CurrencyUnit.USD, Values.Amount.factorize(0.02))));
        assertThat(A.getDateTime(), is(LocalDateTime.parse("2023-04-17T00:00")));
        assertThat(A.getNote(), is("INTEREST ON CREDIT BALANCE AT  0.010% 03/16 THRU 04/15"));
    }

    @Test
    public void testDepotStatement02()
    {
        TastytradePDFExtractor extractor = new TastytradePDFExtractor(new Client());
        List<Exception> errors = new ArrayList<>();
        List<Item> results = extractor.extract(PDFInputFile.loadTestCase(getClass(), "DepotStatement02.txt"), errors);

        assertThat(errors, empty());
        assertThat(results.size(), is(31));
        new AssertImportActions().check(results, CurrencyUnit.USD);

        // check transaction
        Iterator<Extractor.Item> iter = results.stream().filter(BuySellEntryItem.class::isInstance).iterator();
        assertThat(results.stream().filter(BuySellEntryItem.class::isInstance).count(), is(17L));
        assertThat(results.stream().filter(TransactionItem.class::isInstance).count(), is(4L));
        assertThat(results.stream().filter(SecurityItem.class::isInstance).count(), is(10L));

        Item item = iter.next();
        PortfolioTransaction P = ((BuySellEntry) item.getSubject()).getPortfolioTransaction();
        assertThat(P.getSecurity().getCurrencyCode(), is(CurrencyUnit.USD));
        assertThat(P.getCurrencyCode(), is(CurrencyUnit.USD));
        assertThat(P.getType(), is(PortfolioTransaction.Type.BUY));
        assertThat(P.getDateTime(), is(LocalDateTime.parse("2023-06-02T00:00")));
        assertThat(P.getSecurity().getName(), is("TQQQ 16 Jun 2023 32.000 Put"));
        assertThat(P.getNote(), is("PUT TQQQ 06/16/23 32"));
        assertThat(P.getShares(), is(Values.Share.factorize(100)));
        assertThat(P.getGrossPricePerShare().toMoney(), is(Money.of(CurrencyUnit.USD, Values.Amount.factorize(0.42))));
        assertThat(P.getMonetaryAmount(), is(Money.of(CurrencyUnit.USD, Values.Amount.factorize(42.13))));
        assertThat(P.getSecurity().getWkn(), is("9SBMMS8")); // CUSIP
        assertThat(P.getUnitSum(Unit.Type.FEE), is(Money.of(CurrencyUnit.USD, Values.Amount.factorize(0.13))));

        item = iter.next();
        P = ((BuySellEntry) item.getSubject()).getPortfolioTransaction();
        assertThat(P.getSecurity().getCurrencyCode(), is(CurrencyUnit.USD));
        assertThat(P.getCurrencyCode(), is(CurrencyUnit.USD));
        assertThat(P.getType(), is(PortfolioTransaction.Type.SELL));
        assertThat(P.getDateTime(), is(LocalDateTime.parse("2023-06-05T00:00")));
        assertThat(P.getSecurity().getName(), is("TQQQ 16 Jun 2023 35.000 Put"));
        assertThat(P.getNote(), is("PUT TQQQ 06/16/23 35"));
        assertThat(P.getShares(), is(Values.Share.factorize(100)));
        assertThat(P.getGrossPricePerShare().toMoney(), is(Money.of(CurrencyUnit.USD, Values.Amount.factorize(1.01))));
        assertThat(P.getMonetaryAmount(), is(Money.of(CurrencyUnit.USD, Values.Amount.factorize(99.85))));
        assertThat(P.getSecurity().getWkn(), is("9NJWKS7")); // CUSIP
        assertThat(P.getUnitSum(Unit.Type.FEE), is(Money.of(CurrencyUnit.USD, Values.Amount.factorize(1.15))));

        item = iter.next();
        P = ((BuySellEntry) item.getSubject()).getPortfolioTransaction();
        assertThat(P.getSecurity().getCurrencyCode(), is(CurrencyUnit.USD));
        assertThat(P.getCurrencyCode(), is(CurrencyUnit.USD));
        assertThat(P.getType(), is(PortfolioTransaction.Type.BUY));
        assertThat(P.getDateTime(), is(LocalDateTime.parse("2023-06-14T00:00")));
        assertThat(P.getSecurity().getName(), is("PROSHARES TRUST"));
        assertThat(P.getShares(), is(Values.Share.factorize(10)));
        assertThat(P.getGrossPricePerShare().toMoney(), is(Money.of(CurrencyUnit.USD, Values.Amount.factorize(37.61))));
        assertThat(P.getMonetaryAmount(), is(Money.of(CurrencyUnit.USD, Values.Amount.factorize(376.10))));
        assertThat(P.getSecurity().getWkn(), is("74347X831")); // CUSIP
        assertThat(P.getUnitSum(Unit.Type.FEE), is(Money.of(CurrencyUnit.USD, Values.Amount.factorize(0.))));

        item = iter.next();
        P = ((BuySellEntry) item.getSubject()).getPortfolioTransaction();
        assertThat(P.getSecurity().getCurrencyCode(), is(CurrencyUnit.USD));
        assertThat(P.getCurrencyCode(), is(CurrencyUnit.USD));
        assertThat(P.getType(), is(PortfolioTransaction.Type.BUY));
        assertThat(P.getDateTime(), is(LocalDateTime.parse("2023-06-15T00:00")));
        assertThat(P.getSecurity().getName(), is("UPRO 30 Jun 2023 42.000 Put"));
        assertThat(P.getNote(), is("PUT UPRO 06/30/23 42"));
        assertThat(P.getShares(), is(Values.Share.factorize(200)));
        assertThat(P.getGrossPricePerShare().toMoney(), is(Money.of(CurrencyUnit.USD, Values.Amount.factorize(.39))));
        assertThat(P.getMonetaryAmount(), is(Money.of(CurrencyUnit.USD, Values.Amount.factorize(78.26))));
        assertThat(P.getSecurity().getWkn(), is("9UHNHC4")); // CUSIP
        assertThat(P.getUnitSum(Unit.Type.FEE), is(Money.of(CurrencyUnit.USD, Values.Amount.factorize(0.26))));

        item = iter.next();
        item = iter.next();
        item = iter.next();
        item = iter.next();
        item = iter.next();
        item = iter.next();
        item = iter.next();
        item = iter.next();
        item = iter.next();

        P = ((BuySellEntry) item.getSubject()).getPortfolioTransaction();
        assertThat(P.getSecurity().getCurrencyCode(), is(CurrencyUnit.USD));
        assertThat(P.getCurrencyCode(), is(CurrencyUnit.USD));
        assertThat(P.getType(), is(PortfolioTransaction.Type.BUY));
        assertThat(P.getDateTime(), is(LocalDateTime.parse("2023-06-16T00:00")));
        assertThat(P.getSecurity().getName(), is("UPRO 16 Jun 2023 48.000 Call"));
        assertThat(P.getNote(), is("Expired: Buy To Close"));
        assertThat(P.getShares(), is(Values.Share.factorize(200)));
        assertThat(P.getGrossPricePerShare().toMoney(), is(Money.of(CurrencyUnit.USD, Values.Amount.factorize(0.0))));
        assertThat(P.getMonetaryAmount(), is(Money.of(CurrencyUnit.USD, Values.Amount.factorize(0.0))));
        assertThat(P.getSecurity().getWkn(), is("9LPDQC6")); // CUSIP
        assertThat(P.getUnitSum(Unit.Type.FEE), is(Money.of(CurrencyUnit.USD, Values.Amount.factorize(0.0))));

        iter = results.stream().filter(TransactionItem.class::isInstance).iterator();
        item = iter.next();
        AccountTransaction A = (AccountTransaction) item.getSubject();
        assertThat(A.getType(), is(AccountTransaction.Type.INTEREST));
        assertThat(A.getMonetaryAmount(), is(Money.of(CurrencyUnit.USD, Values.Amount.factorize(0.05))));
        assertThat(A.getDateTime(), is(LocalDateTime.parse("2023-06-16T00:00")));
        assertThat(A.getNote(), is("INTEREST ON CREDIT BALANCE AT  0.010% 05/16 THRU 06/15"));

        item = iter.next();
        A = (AccountTransaction) item.getSubject();
        assertThat(A.getType(), is(AccountTransaction.Type.DEPOSIT));
        assertThat(A.getMonetaryAmount(), is(Money.of(CurrencyUnit.USD, Values.Amount.factorize(21000.0))));
        assertThat(A.getDateTime(), is(LocalDateTime.parse("2023-06-13T00:00")));
        assertThat(A.getNote(), is("Journal from account 5WW93590 SEN(20230613044417)"));

        item = iter.next();
        A = (AccountTransaction) item.getSubject();

        assertThat(A.getType(), is(AccountTransaction.Type.DIVIDENDS));
        assertThat(A.getDateTime(), is(LocalDateTime.parse("2023-06-28T00:00")));
        assertThat(A.getShares(), is(Values.Share.factorize(200)));
        assertThat(A.getMonetaryAmount(), is(Money.of(CurrencyUnit.USD, Values.Amount.factorize(20.37))));
        assertThat(A.getUnitSum(Unit.Type.TAX), is(Money.of(CurrencyUnit.USD, Values.Amount.factorize(8.73))));
        assertThat(A.getGrossValue(), is(Money.of(CurrencyUnit.USD, Values.Amount.factorize(29.10))));
        assertThat(A.getSecurity().getWkn(), is("74347X864"));

        item = iter.next();
        A = (AccountTransaction) item.getSubject();
        assertThat(A.getType(), is(AccountTransaction.Type.DIVIDENDS));
        assertThat(A.getDateTime(), is(LocalDateTime.parse("2023-06-28T00:00")));
        assertThat(A.getShares(), is(Values.Share.factorize(10)));
        assertThat(A.getMonetaryAmount(), is(Money.of(CurrencyUnit.USD, Values.Amount.factorize(0.9))));
        assertThat(A.getUnitSum(Unit.Type.TAX), is(Money.of(CurrencyUnit.USD, Values.Amount.factorize(0.38))));
        assertThat(A.getGrossValue(), is(Money.of(CurrencyUnit.USD, Values.Amount.factorize(1.28))));
        assertThat(A.getSecurity().getWkn(), is("74347X831"));
    }

    @Test
    public void testDepotStatement03()
    {
        TastytradePDFExtractor extractor = new TastytradePDFExtractor(new Client());
        List<Exception> errors = new ArrayList<>();
        List<Item> results = extractor.extract(PDFInputFile.loadTestCase(getClass(), "DepotStatement03.txt"), errors);

        assertThat(errors, empty());
        assertThat(results.size(), is(19));
        assertThat(results.stream().filter(BuySellEntryItem.class::isInstance).count(), is(10L));
        assertThat(results.stream().filter(TransactionItem.class::isInstance).count(), is(4L));
        assertThat(results.stream().filter(SecurityItem.class::isInstance).count(), is(5L));
        new AssertImportActions().check(results, CurrencyUnit.USD);

        Iterator<Extractor.Item> iter = results.stream().filter(BuySellEntryItem.class::isInstance).iterator();
        Item item = iter.next();
        item = iter.next();
        item = iter.next();
        item = iter.next();
        item = iter.next();
        item = iter.next();
        item = iter.next();
        item = iter.next();
        item = iter.next();
        item = iter.next();

        PortfolioTransaction P = ((BuySellEntry) item.getSubject()).getPortfolioTransaction();
        assertThat(P.getSecurity().getCurrencyCode(), is(CurrencyUnit.USD));
        assertThat(P.getCurrencyCode(), is(CurrencyUnit.USD));
        assertThat(P.getType(), is(PortfolioTransaction.Type.BUY));
        assertThat(P.getDateTime(), is(LocalDateTime.parse("2023-06-09T00:00")));
        assertThat(P.getSecurity().getName(), is("UPRO 09 Jun 2023 40.000 Call"));
        assertThat(P.getNote(), is("Assigned: Buy To Close"));
        assertThat(P.getShares(), is(Values.Share.factorize(500)));
        assertThat(P.getGrossPricePerShare().toMoney(), is(Money.of(CurrencyUnit.USD, Values.Amount.factorize(0.))));
        assertThat(P.getMonetaryAmount(), is(Money.of(CurrencyUnit.USD, Values.Amount.factorize(0.0))));
        assertThat(P.getSecurity().getWkn(), is("9SMCXK2")); // CUSIP
        assertThat(P.getUnitSum(Unit.Type.FEE), is(Money.of(CurrencyUnit.USD, Values.Amount.factorize(0.0))));
    }
}