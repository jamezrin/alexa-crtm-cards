import com.fasterxml.jackson.databind.deser.impl.BeanPropertyMap;
import com.github.jamezrin.crtmcards.types.CardRenewal;
import com.github.jamezrin.crtmcards.types.CardType;
import com.github.jamezrin.crtmcards.types.CrtmCard;
import org.apache.commons.beanutils.BeanMap;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TestBeanInstr {
    @Test
    public void test1() throws InvocationTargetException, IllegalAccessException {
        CrtmCard card = new CrtmCard("023232", "some title", CardType.NORMAL, new CardRenewal[] {
                new CardRenewal(LocalDate.now(), LocalDate.now(), LocalDate.now(), LocalDate.now()),
                new CardRenewal(LocalDate.now(), LocalDate.now(), LocalDate.now(), LocalDate.now())
        }, LocalDate.now(), null);
        BeanMap result = new BeanMap(card);
        System.out.println(result);

        Map<String, Object> map = result.entrySet().stream().collect(Collectors.toMap(e -> (String)e.getKey(), Map.Entry::getValue));
        CrtmCard card2 = new CrtmCard();
        BeanUtils.populate(card2, map);
    }
}
