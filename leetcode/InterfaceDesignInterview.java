import java.math.BigDecimal;

/**
 * Topic: Interface Design (default/static/private methods)
 *
 * Pro tip:
 * Mention that interfaces in modern Java are not only contracts;
 * they can also host behavior via default methods for API evolution.
 */
public class InterfaceDesignInterview {
    interface TaxPolicy {
        BigDecimal rate();

        /**
         * WHY default method:
         * - Keeps shared logic close to the contract.
         * - Allows adding behavior without breaking implementers.
         */
        default BigDecimal applyTax(BigDecimal amount) {
            return amount.add(amount.multiply(rate()));
        }

        /**
         * WHY static method on interface:
         * - Factory-like helper directly tied to this contract.
         * - Avoids creating a separate util class with weak cohesion.
         */
        static TaxPolicy canadaHst() {
            return () -> new BigDecimal("0.13");
        }

        // Java 9+ allows private interface methods to remove duplication.
        private static void log(String message) {
            System.out.println("[TaxPolicy] " + message);
        }

        static TaxPolicy flat(BigDecimal rate) {
            log("Create flat rate policy: " + rate);
            return () -> rate;
        }
    }

    static class InvoiceService {
        private final TaxPolicy taxPolicy;

        InvoiceService(TaxPolicy taxPolicy) {
            this.taxPolicy = taxPolicy;
        }

        BigDecimal total(BigDecimal subtotal) {
            return taxPolicy.applyTax(subtotal);
        }
    }

    public static void main(String[] args) {
        InvoiceService service = new InvoiceService(TaxPolicy.canadaHst());
        System.out.println(service.total(new BigDecimal("100.00"))); // 113.00
    }
}
