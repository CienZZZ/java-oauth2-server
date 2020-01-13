package pl.weilandt.wms.product.location;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import pl.weilandt.wms.product.Product;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class LocationTest {

    private Product productMock = mock(Product.class);
    private String locationCodePattern = "\\w{3}-\\w{3}-\\w{2}-\\w{2}";

    @Test
    void constructorTest(){
        String expectedCode = "000-000-00-00";
        productMock.setId(123L);

        Location locationCreated = new Location(expectedCode, productMock);
        assertThat(locationCreated.getCode()).isEqualTo(expectedCode);
        assertThat(locationCreated.getProduct()).isEqualTo(productMock);
    }

    @ParameterizedTest
    @MethodSource("goodPatternValues")
    void parameterizedTestOfLocationCodeGoodPatternMatch(String createdPattern){
        assertThat(createdPattern).matches(locationCodePattern);
    }

    @ParameterizedTest
    @MethodSource("wrongPatternValues")
    void parameterizedTestOfLocationCodeWrongPatternNoMatch(String createdPattern){
        assertThat(createdPattern).doesNotMatch(locationCodePattern);
    }

    static Stream<String> goodPatternValues(){
        return Stream.of("000-000-00-01", "234-123-15-11", "abc-432-cd-53", "XCX-232-54-ZZ");
    }

    static Stream<String> wrongPatternValues(){
        return Stream.of("000-000-000-01", "-123-15-11", "abcd-432-cd-53", "XCX-232-54-ZZ-1", "0001112233", "");
    }

}