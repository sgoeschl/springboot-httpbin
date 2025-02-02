/**
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech) (7.12.0-SNAPSHOT).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */
package com.dyrnq.httpbin.api;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import jakarta.annotation.Generated;

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.12.0-SNAPSHOT")
@Validated
@Tag(name = "Performance", description = "Supports performance test related functionality")
public interface PerformanceApi {

    default Optional<NativeWebRequest> getRequest() {
        return Optional.empty();
    }

    /**
     * GET /performance/gc-pause : Simulates a repeated \&quot;stop the world\&quot; GC pause
     *
     * @param responseTime The response time (in milliseconds) when not simulating a GC pause (optional, default to 100)
     * @param intervalRepetition The repeating interval (in seconds) of the simulated GC pausse (optional)
     * @param intervalDuration The amount of time (in seconds) to block all requests during the simulated GC pause (optional)
     * @return A delayed response. (status code 200)
     */
    @Operation(
        operationId = "performanceGcPauseGet",
        summary = "Simulates a repeated \"stop the world\" GC pause",
        tags = { "Performance" },
        responses = {
            @ApiResponse(responseCode = "200", description = "A delayed response.")
        }
    )
    @RequestMapping(
        method = RequestMethod.GET,
        value = "/performance/gc-pause"
    )
    
    default ResponseEntity<Void> performanceGcPauseGet(
        @Parameter(name = "response_time", description = "The response time (in milliseconds) when not simulating a GC pause", in = ParameterIn.QUERY) @Valid @RequestParam(value = "response_time", required = false, defaultValue = "100") Long responseTime,
        @Parameter(name = "interval_repetition", description = "The repeating interval (in seconds) of the simulated GC pausse", in = ParameterIn.QUERY) @Valid @RequestParam(value = "interval_repetition", required = false) Long intervalRepetition,
        @Parameter(name = "interval_duration", description = "The amount of time (in seconds) to block all requests during the simulated GC pause", in = ParameterIn.QUERY) @Valid @RequestParam(value = "interval_duration", required = false) Long intervalDuration
    ) throws Exception {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }

}
