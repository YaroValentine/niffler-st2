package niffler.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;
import java.util.Objects;
import java.util.UUID;

public class SpendJson {
    @JsonProperty("id")
    private UUID id;
    @JsonProperty("spendDate")
    private Date spendDate;
    @JsonProperty("category")
    private String category;
    @JsonProperty("currency")
    private CurrencyValues currency;
    @JsonProperty("amount")
    private Double amount;
    @JsonProperty("description")
    private String description;
    @JsonProperty("username")
    private String username;

    public SpendJson() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Date getSpendDate() {
        return spendDate;
    }

    public void setSpendDate(Date spendDate) {
        this.spendDate = spendDate;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public CurrencyValues getCurrency() {
        return currency;
    }

    public void setCurrency(CurrencyValues currency) {
        this.currency = currency;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "SpendJson{" +
                "spendDate=" + spendDate +
                ", category='" + category + '\'' +
                ", currency=" + currency +
                ", amount=" + amount +
                ", description='" + description + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SpendJson spendJson = (SpendJson) o;
        return Objects.equals(spendDate, spendJson.spendDate) && Objects.equals(category, spendJson.category) && currency == spendJson.currency && Objects.equals(amount, spendJson.amount) && Objects.equals(description, spendJson.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(spendDate, category, currency, amount, description);
    }
}
