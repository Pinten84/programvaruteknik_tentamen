# Lab 11.6.2: Challenge OSPF Configuration - Studyguide

**Estimated Time:** 1 hour  
**Topic:** OSPF Routing Configuration and Verification

---

## Table of Contents
1. [Introduction to OSPF](#introduction-to-ospf)
2. [Task 9: OSPF Routing on Branch2](#task-9-ospf-routing-on-branch2)
3. [Task 10: Verify the Configurations](#task-10-verify-the-configurations)
4. [Task 11: Reflection](#task-11-reflection)
5. [Key Takeaways](#key-takeaways)
6. [Practice Questions](#practice-questions)

---

## Introduction to OSPF

**OSPF (Open Shortest Path First)** is a link-state routing protocol used in IP networks. Key characteristics:

- **Protocol Type:** Link-state (not distance-vector)
- **Algorithm:** Dijkstra's Shortest Path First (SPF)
- **Metric:** Cost (based on bandwidth)
- **Administrative Distance:** 110
- **Multicast Addresses:** 224.0.0.5 (all OSPF routers), 224.0.0.6 (DR/BDR)

### Why OSPF?
- Fast convergence
- Scalable for large networks
- Supports VLSM and CIDR
- No hop count limitation (unlike RIP)
- Hierarchical design using areas

### Network Topology Overview
This lab uses three routers:
- **HQ** - Headquarters router
- **Branch1** - First branch office router
- **Branch2** - Second branch office router

All routers are connected in a partial mesh topology with OSPF Area 0 (backbone area).

---

## Task 9: OSPF Routing on Branch2

### Objective
Configure OSPF on the Branch2 router to advertise its directly connected networks.

### Step 1: Identify Connected Networks

**Question:** What directly connected networks are present in the Branch2 routing table?

**Answer:**
- `172.20.48.0/21` - Local LAN network
- `172.20.56.4/30` - WAN link to HQ
- `172.20.56.8/30` - WAN link to Branch1

**How to verify:**
```cisco
Branch2# show ip route connected
```

### Step 2: Enable OSPF

**Question:** What commands are required to enable OSPF and include the connected networks in the routing updates on Branch2?

**Answer:**
```cisco
Branch2(config)# router ospf 1
Branch2(config-router)# network 172.20.48.0 0.0.7.255 area 0
Branch2(config-router)# network 172.20.56.4 0.0.0.3 area 0
Branch2(config-router)# network 172.20.56.8 0.0.0.3 area 0
```

**Understanding Wildcard Masks:**
- `0.0.7.255` matches `/21` network (2048 addresses)
- `0.0.0.3` matches `/30` network (4 addresses)
- Wildcard mask = inverse of subnet mask

**Why Process ID 1?**
- The process ID (1) is locally significant
- Must match on all routers in the same autonomous system for consistency
- Can be any number from 1-65535

### Step 3: Configure Passive Interfaces

**Question:** Are there any router interfaces on Branch2 that do not need to have OSPF updates sent out?

**Answer:** Yes, **FastEthernet0/0** (the LAN interface facing end devices)

**Reasoning:**
- No other routers are connected to the LAN segment
- Sending OSPF hellos wastes bandwidth and CPU
- Reduces security risk by not advertising routing protocol

**Question:** What command is used to disable OSPF updates on these interfaces?

**Answer:**
```cisco
Branch2(config-router)# passive-interface fa0/0
```

**Benefits of Passive Interfaces:**
- Network is still advertised in OSPF
- No hello packets are sent
- Improved security and efficiency

---

## Task 10: Verify the Configurations

### Connectivity Testing

#### Test 1: PC1 to PC2 Connectivity

**Question:** From PC1, is it possible to ping PC2?

**Answer:** **Yes**

**Why it works:**
- PC1 is on Branch1's LAN (172.20.32.0/20)
- PC2 is on HQ's LAN (172.20.0.0/19)
- OSPF has exchanged routing information between Branch1 and HQ
- Both routers know the path to each other's networks

#### Test 2: PC1 to PC3 Connectivity

**Question:** From PC1, is it possible to ping PC3?

**Answer:** **Yes**

**Why it works:**
- PC1 is on Branch1's LAN
- PC3 is on Branch2's LAN (172.20.48.0/21)
- After configuring OSPF on Branch2, all three routers have full routing tables
- Multiple paths exist (Branch1 can reach Branch2 directly or via HQ)

### Branch1 Routing Table Analysis

**Question:** What OSPF routes are present in the routing table of the Branch1 router?

**Answer:**
- `O 172.20.0.0/19 via 172.20.56.1` - HQ's LAN network
- `O 172.20.48.0/21 via 172.20.56.10` - Branch2's LAN network
- `O 172.20.56.4/30 via 172.20.56.1 and 172.20.56.10` - HQ-to-Branch2 WAN link (2 paths)
- `O*E2 0.0.0.0/0 via 172.20.56.1` - Default route (External Type 2)

**Understanding Route Types:**
- `O` - Intra-area OSPF route
- `O*E2` - OSPF External Type 2 (default route from HQ)

**Question:** What is the gateway of last resort in the routing table of the Branch1 router?

**Answer:** `172.20.56.1` to network `0.0.0.0`

**What is Gateway of Last Resort?**
- The default gateway used when no specific route matches
- Used for Internet-bound traffic
- Points to HQ router which has Internet connectivity

### HQ Routing Table Analysis

**Question:** What OSPF routes are present in the routing table of the HQ router?

**Answer:**
- `O 172.20.32.0/20 via 172.20.56.2` - Branch1's LAN network
- `O 172.20.48.0/21 via 172.20.56.6` - Branch2's LAN network
- `O 172.20.56.8/30 via 172.20.56.2 and 172.20.56.6` - Branch1-to-Branch2 WAN link (2 paths)

**Question:** What is the gateway of last resort in the routing table of the HQ router?

**Answer:** `0.0.0.0` is **directly connected, Loopback1**

**Why different from branches?**
- HQ is the Internet gateway
- Loopback1 represents the Internet connection
- HQ creates and advertises the default route to other routers

### Branch2 Routing Table Analysis

**Question:** What OSPF routes are present in the routing table of the Branch2 router?

**Answer:**
- `O 172.20.0.0/19 via 172.20.56.5` - HQ's LAN network
- `O 172.20.32.0/20 via 172.20.56.9` - Branch1's LAN network
- `O 172.20.56.0/30 via 172.20.56.5 and 172.20.56.9` - HQ-to-Branch1 WAN link (2 paths)
- `O*E2 0.0.0.0/0 via 172.20.56.5` - Default route (External Type 2)

**Question:** What is the gateway of last resort in the routing table of the Branch2 router?

**Answer:** `172.20.56.5` to network `0.0.0.0`

**Observation:** Both Branch1 and Branch2 use HQ as their default gateway for Internet access.

---

## Task 11: Reflection

### Path Analysis

**Question:** What are the hops in the route to PC3 (using tracert from PC1)?

**Answer:**
1. `172.20.32.1` (Branch1 router)
2. `172.20.56.10` (Branch2 router)
3. `172.20.55.254` (PC3)

**Understanding the Path:**
- PC1 sends packets to its default gateway (Branch1)
- Branch1 forwards directly to Branch2 via the `172.20.56.8/30` link
- Branch2 delivers to PC3 on its local LAN

**Question:** Is this the least number of hops that can be used to reach PC3?

**Answer:** **Yes**

**Why is this optimal?**
- Direct link exists between Branch1 and Branch2
- OSPF uses cost metric (based on bandwidth)
- Direct path has lower cost than going through HQ
- Alternative path: PC1 → Branch1 → HQ → Branch2 → PC3 (4 hops)

**Question:** If the answer is no, why is a path with more than the minimum amount of hops used?

**Answer:** **N/A** - This is the shortest path. The direct link between Branch1 and Branch2 is used.

### OSPF Path Selection Criteria

OSPF selects the best path based on:
1. **Cost** (primary metric) = 10^8 / Bandwidth in bps
2. **Equal Cost Multi-Path (ECMP)** - Load balancing across equal-cost paths
3. **Administrative Distance** - Used when comparing OSPF with other protocols

---

## Key Takeaways

### OSPF Configuration Checklist
✅ Enable OSPF process: `router ospf [process-id]`  
✅ Advertise networks: `network [network] [wildcard-mask] area [area-id]`  
✅ Configure passive interfaces on LAN segments  
✅ Verify with: `show ip route`, `show ip ospf neighbor`, `show ip protocols`  

### Important Concepts

1. **Wildcard Masks**
   - Inverse of subnet mask
   - 0 = must match, 1 = don't care
   - Example: `/30` = `255.255.255.252` → wildcard `0.0.0.3`

2. **Passive Interfaces**
   - Stops OSPF hello packets on specified interfaces
   - Network is still advertised
   - Improves security and reduces overhead

3. **Default Route Propagation**
   - HQ originates default route with `default-information originate`
   - Marked as `O*E2` in routing tables
   - Provides Internet access to branch offices

4. **Path Selection**
   - OSPF always prefers lowest cost path
   - Direct links typically have lower cost than multi-hop paths
   - Cost can be manually adjusted with `ip ospf cost` command

### Common Verification Commands

```cisco
show ip route                    # Display routing table
show ip route ospf               # Display only OSPF routes
show ip ospf interface           # OSPF interface details
show ip ospf neighbor            # OSPF neighbor adjacencies
show ip protocols                # Routing protocol status
ping [destination]               # Test connectivity
tracert [destination]            # Trace packet path
```

---

## Practice Questions

### Question 1
What would happen if you forgot to configure `passive-interface fa0/0` on Branch2?

**Answer:** OSPF would send hello packets on the LAN segment, wasting bandwidth and CPU. End devices would receive these packets but couldn't respond, creating unnecessary network traffic.

---

### Question 2
If the link between Branch1 and Branch2 fails, what path would traffic from PC1 to PC3 take?

**Answer:** Traffic would reroute through HQ:
1. PC1 → Branch1 (172.20.32.1)
2. Branch1 → HQ (172.20.56.1)
3. HQ → Branch2 (172.20.56.6)
4. Branch2 → PC3 (172.20.55.254)

OSPF would automatically converge to use this backup path.

---

### Question 3
Why does the wildcard mask for `172.20.48.0/21` equal `0.0.7.255`?

**Answer:**
- Subnet mask: `255.255.248.0` (`11111111.11111111.11111000.00000000`)
- Wildcard mask: `0.0.7.255` (`00000000.00000000.00000111.11111111`)
- The third octet: `248` → binary `11111000`, inverse → `00000111` = `7`
- The fourth octet: `0` → binary `00000000`, inverse → `11111111` = `255`

---

### Question 4
What is the difference between `O` and `O*E2` route codes?

**Answer:**
- `O` = OSPF intra-area route (learned from routers in the same area)
- `O*E2` = OSPF External Type 2 route (redistributed from outside OSPF, like a default route)
- The `*` indicates it's a candidate default route (gateway of last resort)

---

### Question 5
How would you verify OSPF neighbor relationships on Branch2?

**Answer:**
```cisco
Branch2# show ip ospf neighbor
```
You should see two neighbors:
- HQ router (via 172.20.56.5)
- Branch1 router (via 172.20.56.9)

Both should show state `FULL/DR` or `FULL/BDR` (or `FULL/-` on point-to-point links).

---

## Summary

This lab demonstrates:
- ✅ OSPF configuration on multiple routers
- ✅ Network advertisement using wildcard masks
- ✅ Passive interface configuration
- ✅ Routing table verification
- ✅ Path selection and optimization
- ✅ Default route propagation

**Next Steps:**
- Practice configuring OSPF in Packet Tracer or GNS3
- Experiment with route summarization
- Learn about OSPF areas and their benefits
- Study OSPF authentication for enhanced security

---

**Good luck with your studies!** 🎓
