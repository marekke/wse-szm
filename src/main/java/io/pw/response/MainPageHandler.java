package io.pw.response;

import io.pw.WebServer;
import io.pw.db.ProductRepository;
import io.pw.db.Repository;
import io.pw.db.entity.Product;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class MainPageHandler implements ResponseHandler {

    private final Repository<Product> repository = new ProductRepository();

    private static final String ROW_TEMPLATE = """
                <tr>
                    <td>$no</td>
                    <td>$name</td>
                    <td>$desc</td>
                    <td>$serial</td>
                    <td>$qty</td>
                    <td>$reduceButton $deleteButton</td>
                </tr>
            """;
    @Override
    public byte[] handle() {
        try {
            String template = Files.readString(Path.of(WebServer.CONTENT_ROOT, "index.html"));
            String products = translateToHTML();
            return template.replace("<productData />", products).getBytes();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    private String translateToHTML() {
        StringBuilder products = new StringBuilder();
        final List<Product> allProducts = repository.findAll();
        for (int i = 0; i < allProducts.size(); i++) {
            Product product = allProducts.get(i);
            final String reduceButton = """
                    <form method="POST" action="/reduce?id=%d">
                        <button class="btn btn-sm btn-warning">Reduce</button>
                    </form>
                    """.formatted(product.getId());

            final String deleteButton = """
                    <form method="POST" action="/delete?id=%d">
                        <button class="btn btn-sm btn-danger">Delete</button>
                    </form>
                    """.formatted(product.getId());

            String row = ROW_TEMPLATE.replace("$no", String.valueOf(i + 1))
                    .replace("$name", product.getName())
                    .replace("$desc", product.getDesc())
                    .replace("$serial", product.getSerial())
                    .replace("$qty", String.valueOf(product.getQty()))
                    .replace("$reduceButton", reduceButton)
                    .replace("$deleteButton", deleteButton);
            products.append(row);
        }
        return products.toString();
    }
}
