package com.stockpulse.entity;

/**
 * Enum representing the reason that triggered a suggestion.
 */
public enum TriggerReason {
    INITIAL,
    INVENTORY_LOW,
    DEMAND_SPIKE,
    MANUAL
}