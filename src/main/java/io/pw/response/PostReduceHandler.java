package io.pw.response;

import io.pw.db.ProductRepository;
import io.pw.db.Repository;
import io.pw.db.entity.Product;
import io.pw.exception.ProductQtyException;
import io.pw.util.UriParam;
import io.pw.util.UriQueryParser;

import java.net.URI;
import java.util.List;
import java.util.Optional;

public class PostReduceHandler implements ResponseHandler {
    private final URI uri;
    private final Repository<Product> repository = new ProductRepository();
    private final UriQueryParser uriQueryParser = new UriQueryParser();

    public PostReduceHandler(URI uri) {
        this.uri = uri;
    }

    @Override
    public byte[] handle() {
        long productID = -1L;
        List<UriParam> params = uriQueryParser.parse(uri);


        for (UriParam param : params) {
            if (param.getParam().equals("id")) {
                productID = Long.parseLong(param.getValue());
            }
        }

        if (productID == -1) {
            return new byte[0];
        }

        Optional<Product> product = repository.findById(productID);
        if (product.isEmpty()) {
            return new byte[0];
        }

        try {
            product.get().decreaseQty();
        } catch (ProductQtyException e) {
            return new byte[0];
        }

        repository.store(product.get());
        return new byte[0];
    }
}
