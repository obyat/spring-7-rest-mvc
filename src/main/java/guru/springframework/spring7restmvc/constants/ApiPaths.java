package guru.springframework.spring7restmvc.constants;

public final class ApiPaths {

  private ApiPaths() {}

  public static final String API_V1 = "/api/v1";

  public static final class Customer {
    public static final String ROOT = API_V1 + "/customer";
    public static final String BY_ID = "/{customerId}";
    public static final String CUSTOMER_WITH_ID = ROOT + BY_ID;

    private Customer() {}
  }

  public static final class Beer {
    public static final String ROOT = API_V1 + "/beer";
    public static final String BY_ID = "/{beerId}";
    public static final String BEER_WITH_ID = ROOT + BY_ID;

    private Beer() {}
  }
}
