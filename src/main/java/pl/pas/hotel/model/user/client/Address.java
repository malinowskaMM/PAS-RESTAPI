package pl.pas.hotel.model.user.client;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Address {

    @NotNull
    @Size(max = 96)
    @Column(name = "STREET", nullable = false, length = 96)
    private String street;

    @NotNull
    @Size(max = 30)
    @Column(name = "STREET_NUMBER", nullable = false, length = 30)
    private String streetNumber;

    @NotNull
    @Size(max = 40)
    @Column(name = "CITY_NAME", nullable = false, length = 40)
    private String cityName;

    @NotNull
    @Pattern(regexp = "^\\d{2}-\\d{3}$")
    @Column(name = "POSTAL_CODE", nullable = false, columnDefinition = "VARCHAR(6) CHECK (POSTAL_CODE ~ '^\\d{2}-\\d{3}$')")
    private String postalCode;
}
