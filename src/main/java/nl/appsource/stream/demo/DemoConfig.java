package nl.appsource.stream.demo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
//@EnableR2dbcRepositories
//@EnableHypermediaSupport(type = EnableHypermediaSupport.HypermediaType.HAL)
public class DemoConfig /* extends AbstractR2dbcConfiguration */ {

//    @Bean
//    @Override
//    public H2ConnectionFactory connectionFactory() {
//        return new H2ConnectionFactory(
//                H2ConnectionConfiguration.builder()
//                        .url("mem:testdb;DB_CLOSE_DELAY=-1;")
//                        .username("sa")
//                        .build()
//        );
//    }


//    @ReadingConverter
//    public static class PersonReadConverter implements Converter<Row, Citaat> {
//
//        public Citaat convert(Row source) {
//            final Citaat p = new Citaat(
//
//                    (Long) source.get("id"),
//                    source.get("name", String.class)
//            );
//            return p;
//        }
//    }
//
//    @WritingConverter
//    public static class PersonWriteConverter implements Converter<Citaat, OutboundRow> {
//        public OutboundRow convert(Citaat source) {
//            final OutboundRow row = new OutboundRow();
//            row.put("id", SettableValue.from(source.getId()));
//            row.put("name", SettableValue.from(source.getName()));
//            return row;
//        }
//    }
//
//    @Bean
//    @Override
//    public R2dbcCustomConversions r2dbcCustomConversions() {
//        final List<Converter<?, ?>> converterList = new ArrayList<Converter<?, ?>>();
//        converterList.add(new PersonReadConverter());
//        converterList.add(new PersonWriteConverter());
//        return new R2dbcCustomConversions(getStoreConversions(), converterList);
//    }

//    public class CitaatProcessor implements RepresentationModelProcessor<EntityModel<Citaat>> {
//
//        @Override
//        public EntityModel<Citaat> process(EntityModel<Citaat> model) {
//
//            Link link = linkTo(methodOn(Controller.class).getAllOrders()).withSelfRel();
//
//            model.add(link);
//
//            model.add(
//                    new Link("/citaten/{id}").withRel(LinkRelation.of("citaten")) //
//                            .expand(model.getContent().getId()));
//
//            return model;
//        }
//    }
//
//    @Bean
//    public CitaatProcessor getCitaatProcessor() {
//        return new CitaatProcessor();
//    }

}
