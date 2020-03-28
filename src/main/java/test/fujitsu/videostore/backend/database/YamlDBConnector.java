//package test.fujitsu.videostore.backend.database;
//
//import test.fujitsu.videostore.backend.domain.RentOrder;
//
//import java.util.List;
//
//public class YamlDBConnector{
//
//    private String filepath;
//
//    public YamlDBConnector(String filepath) {
//        super(filepath);
//    }
//
//    @Override
//    public void writeSimpleEntityData(List writeData) {
//
//    }
//
//
//    @Override
//    public List<RentOrder> readOrder() {
//        return null;
//    }
//
////    @Override
////    public void writeSimpleEntityData(List<?> writeData, String entityType, Type outputFormatType) {
////        File file1 = new File("db-examples/database.yaml");
////
////        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
////        Map<String, List<Object>> fileMap = null;
////        List<Movie> ol = new ArrayList<>();
////        try {
////            fileMap = mapper.readValue(
////                    file1,
////                    new TypeReference<Map<String, List<Object>>>(){});
////            ol = mapper.convertValue(fileMap.get("movie"), new TypeReference<List<Movie>>(){});
////            ObjectMapper mapper1 = new ObjectMapper(new YAMLFactory().disable(YAMLGenerator.Feature.WRITE_DOC_START_MARKER));
////            mapper1.writeValue(new File("db-examples/database1.yaml"), fileMap);
////        } catch (IOException e) {
////            e.printStackTrace();
////        }
//
//
//
//    @Override
//    public void writeOrderEntity(List<RentOrder> orders) {
//
//    }
//}
