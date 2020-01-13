package pl.weilandt.wms.product;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ProductTest {

    @Test
    void constructorTest(){
        String expectedProductName = "Piwo";
        String expectedProductCode = "piw-321";
        BigDecimal expectedProductQuantity = BigDecimal.valueOf(120);
        String expectedProductUnit = "szt";
        String expectedProductDescription = "Dobre Piwo!";

        Product productCreated = new Product(expectedProductName, expectedProductCode, expectedProductQuantity, expectedProductUnit, expectedProductDescription);
        assertThat(productCreated.getName()).isEqualTo(expectedProductName);
        assertThat(productCreated.getCode()).isEqualTo(expectedProductCode);
        assertThat(productCreated.getQuantity()).isEqualTo(expectedProductQuantity);
        assertThat(productCreated.getUnit()).isEqualTo(expectedProductUnit);
        assertThat(productCreated.getDescription()).isEqualTo(expectedProductDescription);
    }
}