
package com.app.insurance_management.serviceImpl;

import com.app.insurance_management.dto.QuoteRequestDto;
import com.app.insurance_management.dto.QuoteResponseDto;
import com.app.insurance_management.entity.Quote;
import com.app.insurance_management.enums.QuoteStatus;
import com.app.insurance_management.repository.QuoteRepository;
import com.app.insurance_management.service.QuoteService;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class QuoteServiceImpl implements QuoteService {

    private final QuoteRepository quoteRepository;
    private final ModelMapper modelMapper;

    public QuoteServiceImpl(
            QuoteRepository quoteRepository,
            ModelMapper modelMapper) {
        this.quoteRepository = quoteRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public QuoteResponseDto generateQuote(QuoteRequestDto quoteRequestDto) {

        Quote quote = modelMapper.map(quoteRequestDto, Quote.class);

        quote.setQuoteNumber("Q-" + UUID.randomUUID()
                .toString()
                .substring(0, 8)
                .toUpperCase());

        quote.setQuoteDate(LocalDate.now());
        quote.setExpiryDate(LocalDate.now().plusDays(30));
        quote.setQuoteStatus(QuoteStatus.Pending);

        Quote savedQuote = quoteRepository.save(quote);

        return modelMapper.map(savedQuote, QuoteResponseDto.class);
    }

    @Override
    public List<QuoteResponseDto> getAllQuotes() {

        List<Quote> quotes = quoteRepository.findAll();

        return quotes.stream()
                .map(quote -> modelMapper.map(quote, QuoteResponseDto.class))
                .toList();
    }

    @Override
    public QuoteResponseDto getQuoteById(Long id) {

        Quote quote = quoteRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Quote not found with ID: " + id));

        return modelMapper.map(quote, QuoteResponseDto.class);
    }

    @Override
    public QuoteResponseDto updateQuote(Long id, QuoteRequestDto requestDto) {

        Quote quote = quoteRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Quote not found with ID: " + id));

        if (quote.getQuoteStatus() != QuoteStatus.Pending) {
            throw new RuntimeException(
                    "Only pending quotes can be updated");
        }

        if (!quote.getExpiryDate().isAfter(LocalDate.now())) {
            quote.setQuoteStatus(QuoteStatus.Expired);
            quoteRepository.save(quote);

            throw new RuntimeException("Expired quote cannot be updated");
        }

        quote.setCustomerId(requestDto.getCustomerId());
        quote.setProductId(requestDto.getProductId());
        quote.setPolicyTerm(requestDto.getPolicyTerm());
        quote.setSumInsured(requestDto.getSumInsured());
        quote.setPremiumFrequencyId(requestDto.getPremiumFrequencyId());

        Quote updatedQuote = quoteRepository.save(quote);

        return modelMapper.map(updatedQuote, QuoteResponseDto.class);
    }

    @Override
    public void cancelQuote(Long id) {

        Quote quote = quoteRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Quote not found with ID: " + id));

        if (quote.getQuoteStatus() == QuoteStatus.Converted) {
            throw new RuntimeException(
                    "Converted quote cannot be cancelled");
        }

        if (quote.getQuoteStatus() == QuoteStatus.Expired
                || quote.getQuoteStatus() == QuoteStatus.Rejected) {
            throw new RuntimeException(
                    "Expired or rejected quote cannot be cancelled");
        }

        if (quote.getQuoteStatus() == QuoteStatus.Cancelled) {
            throw new RuntimeException("Quote is already cancelled");
        }

        quote.setQuoteStatus(QuoteStatus.Cancelled);
        quoteRepository.save(quote);
    }

    //approval of quote
    @Override
    public QuoteResponseDto approveQuote(Long id) {

        Quote quote = quoteRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Quote not found with ID: " + id));

        if (quote.getQuoteStatus() != QuoteStatus.Pending) {
            throw new RuntimeException(
                    "Only pending quotes can be approved");
        }

        if (!quote.getExpiryDate().isAfter(LocalDate.now())) {
            quote.setQuoteStatus(QuoteStatus.Expired);
            quoteRepository.save(quote);

            throw new RuntimeException(
                    "Expired quote cannot be approved");
        }

        quote.setQuoteStatus(QuoteStatus.Approved);

        Quote savedQuote = quoteRepository.save(quote);

        return modelMapper.map(savedQuote, QuoteResponseDto.class);
    }

    @Override
    public QuoteResponseDto rejectQuote(Long id) {

        Quote quote = quoteRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Quote not found with ID: " + id));

        if (quote.getQuoteStatus() != QuoteStatus.Pending) {
            throw new RuntimeException(
                    "Only pending quotes can be rejected");
        }

        if (!quote.getExpiryDate().isAfter(LocalDate.now())) {
            quote.setQuoteStatus(QuoteStatus.Expired);
            quoteRepository.save(quote);

            throw new RuntimeException(
                    "Expired quote cannot be rejected");
        }

        quote.setQuoteStatus(QuoteStatus.Rejected);

        Quote savedQuote = quoteRepository.save(quote);

        return modelMapper.map(savedQuote, QuoteResponseDto.class);
    }
}
