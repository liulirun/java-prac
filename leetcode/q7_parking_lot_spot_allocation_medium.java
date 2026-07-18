import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Q7) Parking Lot Spot Allocation
 *
 * AI-BEST:
 * Track free contiguous intervals and merge/split on updates.
 * In scalable production designs, tree-based interval structures can approach O(log n).
 *
 * AI-EASY:
 * Linear scan on spot array for a contiguous free block.
 * Time: O(n) park in worst case, Space: O(n)
 */
public class q7_parking_lot_spot_allocation_medium {
    static abstract class Vehicle {
        protected final String licensePlate;

        Vehicle(String licensePlate) {
            this.licensePlate = licensePlate;
        }

        abstract int getRequiredSpots();
    }

    static class Motorcycle extends Vehicle {
        Motorcycle(String licensePlate) { super(licensePlate); }
        @Override int getRequiredSpots() { return 1; }
    }

    static class Car extends Vehicle {
        Car(String licensePlate) { super(licensePlate); }
        @Override int getRequiredSpots() { return 1; }
    }

    static class Truck extends Vehicle {
        Truck(String licensePlate) { super(licensePlate); }
        @Override int getRequiredSpots() { return 3; }
    }

    static class ParkingLotEasy {
        private final String[] spots;

        ParkingLotEasy(int totalSpots) {
            this.spots = new String[totalSpots];
            Arrays.fill(this.spots, "0");
        }

        boolean park(Vehicle vehicle) {
            int need = vehicle.getRequiredSpots();
            for (int i = 0; i <= spots.length - need; i++) {
                boolean allEmpty = true;
                for (int j = i; j < i + need; j++) {
                    if (!"0".equals(spots[j])) {
                        allEmpty = false;
                        break;
                    }
                }
                if (allEmpty) {
                    for (int j = i; j < i + need; j++) spots[j] = vehicle.licensePlate;
                    System.out.println("[EASY] Parked " + vehicle.getClass().getSimpleName() + " " + vehicle.licensePlate
                            + " at " + i + "-" + (i + need - 1));
                    return true;
                }
            }
            System.out.println("[EASY] No room for " + vehicle.licensePlate);
            return false;
        }

        boolean unpark(String licensePlate) {
            boolean removed = false;
            for (int i = 0; i < spots.length; i++) {
                if (licensePlate.equals(spots[i])) {
                    spots[i] = "0";
                    removed = true;
                }
            }
            System.out.println("[EASY] Unpark " + licensePlate + ": " + removed);
            return removed;
        }

        String debugSpots() {
            return String.join(" | ", spots);
        }
    }

    static class Interval {
        int start;
        int end;

        Interval(int start, int end) {
            this.start = start;
            this.end = end;
        }
    }

    static class Allocation {
        final int start;
        final int size;

        Allocation(int start, int size) {
            this.start = start;
            this.size = size;
        }
    }

    static class ParkingLotBest {
        private List<Interval> freeIntervals = new ArrayList<>();
        private final Map<String, Allocation> parked = new HashMap<>();

        ParkingLotBest(int totalSpots) {
            freeIntervals.add(new Interval(0, totalSpots - 1));
        }

        boolean park(Vehicle vehicle) {
            int need = vehicle.getRequiredSpots();
            for (int i = 0; i < freeIntervals.size(); i++) {
                Interval interval = freeIntervals.get(i);
                int available = interval.end - interval.start + 1;
                if (available >= need) {
                    int start = interval.start;
                    int end = start + need - 1;

                    parked.put(vehicle.licensePlate, new Allocation(start, need));
                    if (end == interval.end) {
                        freeIntervals.remove(i);
                    } else {
                        interval.start = end + 1;
                    }

                    System.out.println("[BEST] Parked " + vehicle.getClass().getSimpleName() + " "
                            + vehicle.licensePlate + " at " + start + "-" + end);
                    return true;
                }
            }

            System.out.println("[BEST] No room for " + vehicle.licensePlate);
            return false;
        }

        boolean unpark(String licensePlate) {
            Allocation record = parked.remove(licensePlate);
            if (record == null) {
                System.out.println("[BEST] Unpark " + licensePlate + ": false");
                return false;
            }

            freeIntervals.add(new Interval(record.start, record.start + record.size - 1));
            freeIntervals.sort(Comparator.comparingInt(a -> a.start));

            List<Interval> merged = new ArrayList<>();
            for (Interval cur : freeIntervals) {
                if (merged.isEmpty() || merged.get(merged.size() - 1).end + 1 < cur.start) {
                    merged.add(new Interval(cur.start, cur.end));
                } else {
                    Interval last = merged.get(merged.size() - 1);
                    last.end = Math.max(last.end, cur.end);
                }
            }
            freeIntervals = merged;

            System.out.println("[BEST] Unpark " + licensePlate + ": true");
            return true;
        }

        String debugFreeIntervals() {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < freeIntervals.size(); i++) {
                Interval in = freeIntervals.get(i);
                if (i > 0) sb.append(", ");
                sb.append("[").append(in.start).append("-").append(in.end).append("]");
            }
            return sb.toString();
        }
    }

    public static void main(String[] args) {
        System.out.println("Q7: Parking Lot Spot Allocation");
        Vehicle[] vehicles = {
                new Car("ABC-123"),
                new Truck("BIG-TRK"),
                new Motorcycle("M-42"),
                new Car("CAR-999")
        };

        System.out.println("-EASY-");
        ParkingLotEasy easyLot = new ParkingLotEasy(8);
        for (Vehicle v : vehicles) easyLot.park(v);
        easyLot.unpark("BIG-TRK");
        easyLot.park(new Truck("TRK-2"));
        System.out.println("[EASY] Spots: " + easyLot.debugSpots());

        System.out.println("-BEST-");
        ParkingLotBest bestLot = new ParkingLotBest(8);
        for (Vehicle v : vehicles) bestLot.park(v);
        System.out.println("[BEST] Free: " + bestLot.debugFreeIntervals());
        bestLot.unpark("BIG-TRK");
        System.out.println("[BEST] Free after unpark: " + bestLot.debugFreeIntervals());
        bestLot.park(new Truck("TRK-2"));
        System.out.println("[BEST] Free final: " + bestLot.debugFreeIntervals());
    }
}


