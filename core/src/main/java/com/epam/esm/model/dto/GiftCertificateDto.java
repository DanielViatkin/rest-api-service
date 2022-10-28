package com.epam.esm.model.dto;

import com.fasterxml.jackson.annotation.*;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;
import javax.validation.constraints.*;


import java.math.BigDecimal;
import java.time.Instant;
import java.util.Set;

@Data
@AllArgsConstructor
public class GiftCertificateDto extends RepresentationModel<GiftCertificateDto> {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;
    @NotNull(message = "certificate.invalid.data")
    private String name;
    @NotNull(message = "certificate.invalid.data")
    private String description;
    @NotNull(message = "certificate.invalid.data")
    private BigDecimal price;
    @NotNull(message = "certificate.invalid.data")
    private Integer duration;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'hh:mm:ss.SSS", timezone = "UTC")
    private Instant createDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'hh:mm:ss.SSS", timezone = "UTC")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Instant lastUpdateDate;
    private Set<TagDto> tags;

    @JsonCreator
    public GiftCertificateDto(){
    }
}
