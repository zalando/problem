---
layout: page
title: Constraint Violation
---

A problems that indicates a syntactically correct, yet semantically illegal request. It's **not** meant to be used for end-user input validation, but for client developer convenience. Any constraint violation problem happening in production should be considered a bug.

```yaml
{
    "type": "https://github.com/zalando/problem/wiki/constraint-violation",
    "title": "Constraint Violation",
    "status": 422,
    "violations": [
        {
            "field": "billing_address.first_name",
            "message": "may not be empty"
        },
        {
            "field": "billing_address.zip",
            "message": "may not be empty"
        },
        {
            "field": "groups",
            "message": "must not contain multiple different delivery addresses"
        },
        {
            "field": "groups[0].delivery_options.delivery_service",
            "message": "not supported"
        },
        {
            "field": "groups[0].delivery_options.destination",
            "message": "limited to one of the following types (for now): https://docs.pennybags.zalan.do/delivery-destination/home"
        },
        {
            "field": "groups[0].delivery_options.destination.address.street",
            "message": "hack no longer supported; use locker delivery destination"
        },
        {
            "field": "groups[0].delivery_options.destination.service_point",
            "message": "not supported"
        },
        {
            "field": "groups[0].delivery_options.expected_delivery_date",
            "message": "not supported"
        },
        {
            "field": "groups[0].delivery_options.parcel_service",
            "message": "not supported"
        },
        {
            "field": "groups[0].items[0].gift_options",
            "message": "not supported"
        },
        {
            "field": "groups[0].items[0].name",
            "message": "may not be empty"
        },
        {
            "field": "groups[0].items[0].sku",
            "message": "must be a valid SKU"
        },
        {
            "field": "groups[0].items[0].totals.gross_total",
            "message": "may not contain multiple currencies"
        },
        {
            "field": "groups[0].items[0].transactions[1].gross_amount",
            "message": "may not contain multiple currencies"
        },
        {
            "field": "groups[0].items[0].transactions[1].id",
            "message": "must be unique"
        },
        {
            "field": "groups[0].items[0].transactions[1].links",
            "message": "may not contain multiple coupons"
        },
        {
            "field": "groups[0].items[0].transactions[2]",
            "message": "not supported at this location"
        },
        {
            "field": "groups[0].items[0].value",
            "message": "not supported"
        },
        {
            "field": "groups[0].links",
            "message": "must not contain any https://docs.pennybags.zalan.do/legacy/warehouse link"
        },
        {
            "field": "groups[0].totals.tax_total",
            "message": "may not contain multiple currencies"
        },
        {
            "field": "groups[0].transactions[0]",
            "message": "not supported at this location"
        },
        {
            "field": "groups[0].transactions[0].gross_amount",
            "message": "may not be null"
        },
        {
            "field": "groups[1].delivery_options",
            "message": "limited to one of the following types (for now): https://docs.pennybags.zalan.do/delivery-method/parcel"
        },
        {
            "field": "groups[1].delivery_options.destination.address.additional",
            "message": "hack no longer supported; use service point delivery destination"
        },
        {
            "field": "groups[1].delivery_options.destination.address.street",
            "message": "may not be empty"
        },
        {
            "field": "groups[1].items[0].transactions[1]",
            "message": "gross/tax amounts must have consistent signs"
        },
        {
            "field": "groups[1].items[0].transactions[1].id",
            "message": "must be unique"
        },
        {
            "field": "groups[1].items[0].transactions[1].links",
            "message": "may not contain multiple coupons"
        },
        {
            "field": "groups[1].totals",
            "message": "gross/tax amounts must have consistent signs"
        },
        {
            "field": "groups[1].transactions[0].tax_amount",
            "message": "may not be null"
        },
        {
            "field": "links",
            "message": "https://docs.pennybags.zalan.do/legacy/payment-method links to CREDITCARD must contain the following parameters: card_holder, type, obfuscated_number, valid_until, pseudo_card_pan"
        },
        {
            "field": "links",
            "message": "must contain at least 1 https://docs.pennybags.zalan.do/legacy/app-domain link(s)"
        },
        {
            "field": "links",
            "message": "must contain at least 1 https://docs.pennybags.zalan.do/legacy/channel link(s)"
        },
        {
            "field": "links",
            "message": "must contain at least 1 https://docs.pennybags.zalan.do/legacy/customer link(s)"
        },
        {
            "field": "links",
            "message": "must contain at least 1 https://docs.pennybags.zalan.do/legacy/error-page link(s)"
        },
        {
            "field": "links",
            "message": "must contain at most 1 https://docs.pennybags.zalan.do/legacy/cancel-page link(s)"
        },
        {
            "field": "payments[0].gross_amount",
            "message": "may not be null"
        },
        {
            "field": "signature",
            "message": "must match signed properties"
        },
        {
            "field": "totals.gross_total",
            "message": "may not contain multiple currencies"
        },
        {
            "field": "transactions[0]",
            "message": "not supported at this location"
        },
        {
            "field": "transactions[1]",
            "message": "not supported at this location"
        },
        {
            "field": "transactions[1].tax_amount",
            "message": "may not be null"
        }
    ]
}
```