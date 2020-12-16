package nl.appsource.stream.demo.config;

import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactory;
import io.r2dbc.spi.Row;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.appsource.stream.demo.model.Categorie;
import nl.appsource.stream.demo.model.Citaat;
import nl.appsource.stream.demo.model.Spreker;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterRegistry;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration;
import org.springframework.data.r2dbc.mapping.OutboundRow;
import org.springframework.r2dbc.core.Parameter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class ConverterConfig extends AbstractR2dbcConfiguration {

    private final ConverterRegistry converterRegistry;

    @Value("${spring.r2dbc.url}")
    private String r2dbcUrl;

    @Override
    public ConnectionFactory connectionFactory() {
        return ConnectionFactories.get(r2dbcUrl);
    }

    @Override
    protected List<Object> getCustomConverters() {

        final List<Object> converterList = new ArrayList<>();

        converterList.add(new CitaatReadConverter());
        converterList.add(new CitaatWriteConverter());

        converterList.add(new CategorieReadConverter());
        converterList.add(new CategorieWriteConverter());

        converterList.add(new SprekerReadConverter());
        converterList.add(new SprekerWriteConverter());

        return converterList;
    }

    @ReadingConverter
    public static class CitaatReadConverter implements Converter<Row, Citaat> {
        public Citaat convert(Row source) {
            return new Citaat(decode((byte[]) source.get("id")), (String) source.get("name"), decode((byte[]) source.get("author_id")), decode((byte[]) source.get("genre_id")));
        }
    }


    @WritingConverter
    public static class CitaatWriteConverter implements Converter<Citaat, OutboundRow> {
        public OutboundRow convert(Citaat source) {
            OutboundRow row = new OutboundRow();
            row.put("id", Parameter.from(encode(source.getId())));
            row.put("name", Parameter.from(source.getName()));
            row.put("author", Parameter.from(encode(source.getSpreker())));
            row.put("genre", Parameter.from(encode(source.getCategorie())));
            return row;
        }
    }

    @ReadingConverter
    public static class CategorieReadConverter implements Converter<Row, Categorie> {
        public Categorie convert(Row source) {
            return new Categorie(decode((byte[]) source.get("id")), (String) source.get("name"));
        }
    }

    @WritingConverter
    public static class CategorieWriteConverter implements Converter<Categorie, OutboundRow> {
        public OutboundRow convert(Categorie source) {
            OutboundRow row = new OutboundRow();
            row.put("id", Parameter.from(encode(source.getId())));
            row.put("name", Parameter.from(source.getName()));
            return row;
        }
    }

    @ReadingConverter
    public static class SprekerReadConverter implements Converter<Row, Spreker> {
        public Spreker convert(Row source) {
            return new Spreker(decode((byte[]) source.get("id")), (String) source.get("name"));
        }
    }

    @WritingConverter
    public static class SprekerWriteConverter implements Converter<Spreker, OutboundRow> {
        public OutboundRow convert(Spreker source) {
            OutboundRow row = new OutboundRow();
            row.put("id", Parameter.from(encode(source.getId())));
            row.put("name", Parameter.from(source.getName()));
            return row;
        }
    }

    private static byte[] encode(final UUID uuid) {

        final byte[] bytes = new byte[16];
        final long msb = uuid.getMostSignificantBits();
        final long lsb = uuid.getLeastSignificantBits();

        bytes[0x0] = (byte) (msb >>> 56);
        bytes[0x1] = (byte) (msb >>> 48);
        bytes[0x2] = (byte) (msb >>> 40);
        bytes[0x3] = (byte) (msb >>> 32);
        bytes[0x4] = (byte) (msb >>> 24);
        bytes[0x5] = (byte) (msb >>> 16);
        bytes[0x6] = (byte) (msb >>> 8);
        bytes[0x7] = (byte) (msb);

        bytes[0x8] = (byte) (lsb >>> 56);
        bytes[0x9] = (byte) (lsb >>> 48);
        bytes[0xa] = (byte) (lsb >>> 40);
        bytes[0xb] = (byte) (lsb >>> 32);
        bytes[0xc] = (byte) (lsb >>> 24);
        bytes[0xd] = (byte) (lsb >>> 16);
        bytes[0xe] = (byte) (lsb >>> 8);
        bytes[0xf] = (byte) (lsb);

        return bytes;
    }

    private static UUID decode(final byte[] bytes) {

        long msb = 0;
        long lsb = 0;

        msb |= (bytes[0x0] & 0xffL) << 56;
        msb |= (bytes[0x1] & 0xffL) << 48;
        msb |= (bytes[0x2] & 0xffL) << 40;
        msb |= (bytes[0x3] & 0xffL) << 32;
        msb |= (bytes[0x4] & 0xffL) << 24;
        msb |= (bytes[0x5] & 0xffL) << 16;
        msb |= (bytes[0x6] & 0xffL) << 8;
        msb |= (bytes[0x7] & 0xffL);

        lsb |= (bytes[0x8] & 0xffL) << 56;
        lsb |= (bytes[0x9] & 0xffL) << 48;
        lsb |= (bytes[0xa] & 0xffL) << 40;
        lsb |= (bytes[0xb] & 0xffL) << 32;
        lsb |= (bytes[0xc] & 0xffL) << 24;
        lsb |= (bytes[0xd] & 0xffL) << 16;
        lsb |= (bytes[0xe] & 0xffL) << 8;
        lsb |= (bytes[0xf] & 0xffL);

        return new UUID(msb, lsb);
    }

}
