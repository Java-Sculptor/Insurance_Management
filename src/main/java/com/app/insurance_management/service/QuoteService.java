
package com.app.insurance_management.service;

import com.app.insurance_management.dto.QuoteRequestDto;
import com.app.insurance_management.dto.QuoteResponseDto;

import java.util.List;

public interface QuoteService {

    QuoteResponseDto generateQuote(QuoteRequestDto quoteRequestDto);

    List<QuoteResponseDto> getAllQuotes();

    QuoteResponseDto getQuoteById(Long id);

    QuoteResponseDto updateQuote(Long id, QuoteRequestDto requestDto);

    void cancelQuote(Long id);


    //approve quote

    QuoteResponseDto approveQuote(Long id);

    QuoteResponseDto rejectQuote(Long id);

}
