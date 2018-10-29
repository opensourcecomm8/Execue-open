Field Delimiter is #*#
With in a field comma is the separator

1. discover_bed_details.csv
    This file contains the information from the application for all the entities (Concepts only)
    Attributes are.,
        Name of the entity (name only, not the display name)
        BED ID of the entity
        KDX DATA TYPE of the entity (one of the "ID"|"SL"|"DIMENSION"|"MEASURE")
        Variations 1 of the entity (Display Name, Description, Column Name and any other synonyms created in the SWI)
        Variations 2 of the entity (Display Name, Description, Column Name and any other synonyms created in the SWI)

2. discover_past_usage.csv
    This file contains the information of query history in the form of entity name(s) and/or any of the variation form(s) or even BED_ID(s)
    Attributes are.,
        Serial Number of the query pattern(s), which is of no significance but needed in the file
        Select(s)
        Group By(s)
        Where Condition(s)
        Usage Count
 